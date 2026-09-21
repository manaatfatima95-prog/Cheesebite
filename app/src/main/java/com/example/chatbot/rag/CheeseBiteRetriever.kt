package com.example.chatbot.rag

import com.example.chatbot.data.CheeseBiteKnowledgeBase
import com.example.chatbot.model.KnowledgeChunk
import com.example.chatbot.model.QueryIntent
import com.example.chatbot.model.RetrievalResult
import java.util.Locale

object CheeseBiteRetriever {

    private val GREETING_KEYWORDS = listOf(
        "hi", "hello", "hey", "hola", "good morning", "good evening", "good afternoon"
    )

    private val SALAM_KEYWORDS = listOf(
        "assalam", "assalamualaikum", "salam", "slm", "asalam", "aslam"
    )

    private val THANKS_KEYWORDS = listOf(
        "thank", "thanks", "thank you", "shukriya", "jazakallah", "jazak allah"
    )

    private val OKAY_KEYWORDS = listOf(
        "ok", "okay", "alright", "sure", "got it", "fine", "acha", "theek", "thik"
    )

    private val OUT_OF_SCOPE_PATTERNS = listOf(
        "president", "prime minister", "politics", "python", "programming", "code", "java", "c++",
        "joke", "funny", "poem", "poetry", "song", "weather", "rain", "temperature", "forecast",
        "movie", "cinema", "cricket", "football", "match", "world cup", "math", "solve",
        "capital of", "who wrote", "history of pakistan", "how to learn", "translate", "homework"
    )

    // Specific unknown restaurant details that are NOT in the Cheese Bites knowledge base
    private val INGREDIENT_KEYWORDS = listOf("ingredient", "ingredients", "recipe", "spices", "sauce", "what is in", "what contains")
    private val DELIVERY_TIME_KEYWORDS = listOf("how long does delivery take", "delivery time", "delivery minutes", "how fast", "when will it arrive", "time take")
    private val PAYMENT_METHOD_KEYWORDS = listOf("jazzcash", "easypaisa", "credit card", "debit card", "payment method", "online payment", "pay with card", "card payment")
    private val DELIVERY_AREA_KEYWORDS = listOf("delivery area", "delivery radius", "which areas", "where do you deliver", "deliver to", "delivery coverage", "delivery location")
    private val NUTRITION_KEYWORDS = listOf("calorie", "calories", "nutrition", "carbs", "protein", "diet", "healthy", "fat", "sugar")

    private val REDIRECTION_VARIATIONS = listOf(
        "I’d be happy to help you with Cheese Bites! 😊 I can assist with our menu, prices, deals, opening hours, location, and home delivery information. What would you like to know?",
        "I’m here to help you with Cheese Bites 😊. I can provide information about our menu, prices, deals, opening hours, location, and delivery.",
        "Let’s get you the information you need about Cheese Bites! You can ask me about our menu, prices, deals, location, or opening hours.",
        "I can help with Cheese Bites-related questions. Would you like to know about our menu, pizza sizes, deals, or delivery?"
    )

    private var variationIndex = 0

    private fun getNextRedirection(): String {
        val variation = REDIRECTION_VARIATIONS[variationIndex % REDIRECTION_VARIATIONS.size]
        variationIndex++
        return variation
    }

    /**
     * Retrieve knowledge and determine intent according to strict RAG guidelines.
     */
    fun retrieve(query: String): RetrievalResult {
        val cleanQuery = query.trim().lowercase(Locale.ROOT)
        val words = cleanQuery.split(Regex("[\\s,?.!]+")).filter { it.isNotBlank() }

        // 1. Check Salam Greetings
        if (SALAM_KEYWORDS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.GREETING,
                relevantChunks = emptyList(),
                conversationalPrompt = "Wa Alaikum Assalam! 👋 Welcome to Cheese Bites. How can I help you today?"
            )
        }

        // 2. Check Standard Greetings
        if (GREETING_KEYWORDS.any { greeting -> words.contains(greeting) || cleanQuery == greeting }) {
            return RetrievalResult(
                intent = QueryIntent.GREETING,
                relevantChunks = emptyList(),
                conversationalPrompt = "Hi! 👋 Welcome to Cheese Bites. How can I help you with our menu, deals, prices, or delivery information?"
            )
        }

        // 3. Check Thanks
        if (THANKS_KEYWORDS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.PLEASANTRY,
                relevantChunks = emptyList(),
                conversationalPrompt = "You're very welcome! 😊 If you need any information about Cheese Bites, I'm happy to help."
            )
        }

        // 4. Check Okay / Acknowledgements
        if (OKAY_KEYWORDS.any { cleanQuery == it || words.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.PLEASANTRY,
                relevantChunks = emptyList(),
                conversationalPrompt = "Absolutely! Let me know if you'd like to check our menu, prices, deals, or other Cheese Bites information."
            )
        }

        // 5. Check Out-of-Scope non-restaurant queries
        if (OUT_OF_SCOPE_PATTERNS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.OUT_OF_SCOPE,
                relevantChunks = emptyList(),
                conversationalPrompt = getNextRedirection()
            )
        }

        // 6. Check Specific Unknown Restaurant Details (Strict Anti-Hallucination - Part 6)
        if (INGREDIENT_KEYWORDS.any { cleanQuery.contains(it) }) {
            // Find item name if mentioned (e.g. zinger burger)
            val itemName = findMentionedItem(cleanQuery) ?: "our menu items"
            return RetrievalResult(
                intent = QueryIntent.UNKNOWN_RESTAURANT_DETAIL,
                relevantChunks = findRelevantChunks(cleanQuery),
                unknownDetailType = "ingredients for $itemName",
                conversationalPrompt = "I’d be happy to help with Cheese Bites! I don’t have the ingredient details for the $itemName in my available information. I can, however, provide its price or help you explore other menu items."
            )
        }

        if (DELIVERY_TIME_KEYWORDS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.UNKNOWN_RESTAURANT_DETAIL,
                relevantChunks = CheeseBiteKnowledgeBase.getChunksByCategory(com.example.chatbot.model.KnowledgeCategory.SERVICES),
                unknownDetailType = "delivery time",
                conversationalPrompt = "I’d be happy to help with Cheese Bites! While we provide Free Home Delivery, our available information does not specify estimated delivery times. You can contact us directly at 0320-9163877 or 0306-7526655 for live order status!"
            )
        }

        if (PAYMENT_METHOD_KEYWORDS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.UNKNOWN_RESTAURANT_DETAIL,
                relevantChunks = emptyList(),
                unknownDetailType = "payment methods",
                conversationalPrompt = "I’d be happy to help with Cheese Bites! Our available information does not specify accepted online payment methods like JazzCash or card payments. You can reach us at 0320-9163877 or 0306-7526655 for payment inquiries, or ask me about our menu, prices, and deals!"
            )
        }

        if (DELIVERY_AREA_KEYWORDS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.UNKNOWN_RESTAURANT_DETAIL,
                relevantChunks = CheeseBiteKnowledgeBase.getChunksByCategory(com.example.chatbot.model.KnowledgeCategory.LOCATION),
                unknownDetailType = "delivery area",
                conversationalPrompt = "We offer Free Home Delivery from our restaurant located at Mandi Throo, Near Hamza Traders, Zafarwal Road. Our available information does not list specific delivery area boundaries, but you can confirm delivery to your location by calling 0320-9163877 or 0306-7526655!"
            )
        }

        if (NUTRITION_KEYWORDS.any { cleanQuery.contains(it) }) {
            return RetrievalResult(
                intent = QueryIntent.UNKNOWN_RESTAURANT_DETAIL,
                relevantChunks = emptyList(),
                unknownDetailType = "nutritional information",
                conversationalPrompt = "I’d be happy to help with Cheese Bites! Our available information does not include calorie or nutritional details. I can, however, assist you with item prices, pizza sizes, deals, and opening hours."
            )
        }

        // 7. Standard Knowledge Retrieval (RAG)
        val rankedChunks = findRelevantChunks(cleanQuery)
        if (rankedChunks.isEmpty()) {
            // No matching Cheese Bites topic found -> Polite redirect
            return RetrievalResult(
                intent = QueryIntent.OUT_OF_SCOPE,
                relevantChunks = emptyList(),
                conversationalPrompt = getNextRedirection()
            )
        }

        return RetrievalResult(
            intent = QueryIntent.RESTAURANT_FACTUAL,
            relevantChunks = rankedChunks
        )
    }

    private fun findMentionedItem(query: String): String? {
        val items = listOf(
            "zinger burger", "chicken petty burger", "double doser", "grill burger", "sizzelar burger",
            "tower burger", "pizza burger", "cheese stick", "cheese panni pasta", "al frado pasta",
            "cheese kanafa", "russian salad", "regular fries", "loaded fries", "hot wings", "nuggets",
            "chicken tika", "chicken kabab", "chicken malai boti", "beef kabab", "special bite pizza",
            "doner pizza", "tikka wrap", "fajita wrap", "chicken shawarma", "zinger shawarma"
        )
        return items.firstOrNull { query.contains(it) }?.replaceFirstChar { it.uppercase() }
    }

    /**
     * Score chunks based on query tokens and keyword matches.
     */
    private fun findRelevantChunks(query: String): List<KnowledgeChunk> {
        val allChunks = CheeseBiteKnowledgeBase.getAllChunks()
        val queryTokens = query.split(Regex("[\\s,?.!]+"))
            .filter { it.length > 1 }
            .map { it.lowercase(Locale.ROOT) }

        val scored = allChunks.map { chunk ->
            var score = 0.0
            val contentLower = chunk.content.lowercase(Locale.ROOT)
            val titleLower = chunk.title.lowercase(Locale.ROOT)

            // Direct phrase match
            if (query.length > 3 && contentLower.contains(query)) {
                score += 15.0
            }
            if (query.length > 3 && titleLower.contains(query)) {
                score += 20.0
            }

            // Keyword matches
            for (kw in chunk.keywords) {
                if (query.contains(kw)) {
                    score += 10.0 + kw.length
                }
            }

            // Token overlap
            for (token in queryTokens) {
                if (titleLower.contains(token)) {
                    score += 4.0
                } else if (contentLower.contains(token)) {
                    score += 1.5
                }
            }

            // Category-specific semantic query matches
            if ((query.contains("time") || query.contains("timing") || query.contains("hour") || query.contains("open") || query.contains("close")) &&
                chunk.category == com.example.chatbot.model.KnowledgeCategory.OPENING_HOURS
            ) {
                score += 12.0
            }

            if ((query.contains("where") || query.contains("location") || query.contains("address") || query.contains("located") || query.contains("road")) &&
                chunk.category == com.example.chatbot.model.KnowledgeCategory.LOCATION
            ) {
                score += 15.0
            }

            if ((query.contains("delivery") || query.contains("home delivery") || query.contains("deliver")) &&
                chunk.category == com.example.chatbot.model.KnowledgeCategory.SERVICES
            ) {
                score += 12.0
            }

            if ((query.contains("phone") || query.contains("call") || query.contains("number") || query.contains("contact")) &&
                chunk.category == com.example.chatbot.model.KnowledgeCategory.CONTACT
            ) {
                score += 12.0
            }

            if ((query.contains("deal") || query.contains("deals") || query.contains("package") || query.contains("offer")) &&
                chunk.category == com.example.chatbot.model.KnowledgeCategory.DEALS
            ) {
                score += 8.0
            }

            if ((query.contains("pizza") || query.contains("pizzas") || query.contains("crust")) &&
                chunk.category == com.example.chatbot.model.KnowledgeCategory.PIZZAS
            ) {
                score += 8.0
            }

            if ((query.contains("bbq") || query.contains("barbecue") || query.contains("bar.b.q") || query.contains("boti") || query.contains("tika")) &&
                chunk.id == "menu_bbq"
            ) {
                score += 15.0
            }

            if ((query.contains("ceo") || query.contains("owner") || query.contains("subhani")) &&
                chunk.id == "gen_ceo"
            ) {
                score += 20.0
            }

            if ((query.contains("branch") || query.contains("announcement") || query.contains("new branch")) &&
                chunk.id == "gen_announcement"
            ) {
                score += 20.0
            }

            chunk to score
        }

        return scored.filter { it.second >= 3.0 }
            .sortedByDescending { it.second }
            .take(4)
            .map { it.first }
    }
}
