package com.example.chatbot.rag

import com.example.BuildConfig
import com.example.chatbot.model.QueryIntent
import com.example.chatbot.model.RetrievalResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Service to execute Grounded AI generation using Gemini API (gemini-3.5-flash).
 * Injects retrieved Cheese Bites knowledge chunks into system prompt and request context.
 * Falls back to local grounded generator if offline or API key is missing.
 */
class GeminiRAGService(
    private val apiKey: String = BuildConfig.GEMINI_API_KEY
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateGroundedResponse(
        query: String,
        retrievalResult: RetrievalResult
    ): String = withContext(Dispatchers.IO) {
        // Fast paths for conversational pleasantries, out of scope, or specific unknown restaurant details
        if (retrievalResult.intent != QueryIntent.RESTAURANT_FACTUAL) {
            return@withContext CheeseBiteLocalGroundedGenerator.generateResponse(query, retrievalResult)
        }

        // If no API key configured or is placeholder, use deterministic local grounded synthesizer
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext CheeseBiteLocalGroundedGenerator.generateResponse(query, retrievalResult)
        }

        try {
            // Build strictly grounded context from retrieved chunks
            val contextText = buildString {
                appendLine("=== AUTHORIZED CHEESE BITES RESTAURANT KNOWLEDGE ===")
                retrievalResult.relevantChunks.forEach { chunk ->
                    appendLine("[Category: ${chunk.category.displayName} | Title: ${chunk.title}]")
                    appendLine(chunk.content)
                    appendLine()
                }
                appendLine("====================================================")
            }

            val systemPrompt = """
                You are the professional, friendly, and helpful AI Assistant for Cheese Bites restaurant.
                
                STRICT RULES:
                1. Answer the customer's question using ONLY the provided AUTHORIZED CHEESE BITES RESTAURANT KNOWLEDGE.
                2. Do NOT invent, assume, extrapolate, or hallucinate any menu item, price, ingredient, delivery time, delivery area, or policy that is not explicitly in the provided knowledge.
                3. If the user asks for information not in the knowledge base, do NOT guess. Politely state that the available Cheese Bites information does not include that detail and redirect them to available menu items, prices, deals, or opening hours.
                4. Tone: Polite, respectful, warm, professional, and customer-focused.
                5. Keep responses concise and easy to read on a mobile device.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$contextText\n\nCustomer Question: $query")
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                })

                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("topP", 0.95)
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    // Fallback to local grounded generator on API error
                    return@withContext CheeseBiteLocalGroundedGenerator.generateResponse(query, retrievalResult)
                }

                val bodyString = response.body?.string().orEmpty()
                val responseJson = JSONObject(bodyString)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    text.trim()
                } else {
                    CheeseBiteLocalGroundedGenerator.generateResponse(query, retrievalResult)
                }
            }
        } catch (e: Exception) {
            // In case of network timeout or error, fallback safely without crash
            CheeseBiteLocalGroundedGenerator.generateResponse(query, retrievalResult)
        }
    }
}
