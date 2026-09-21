package com.example

import com.example.chatbot.data.CheeseBiteKnowledgeBase
import com.example.chatbot.model.KnowledgeCategory
import com.example.chatbot.model.KnowledgeChunk
import com.example.chatbot.model.QueryIntent
import com.example.chatbot.rag.CheeseBiteLocalGroundedGenerator
import com.example.chatbot.rag.CheeseBiteRetriever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CheeseBiteRAGTest {

    @Test
    fun `test knowledge base contains all required categories and chunks`() {
        val allChunks = CheeseBiteKnowledgeBase.getAllChunks()
        assertTrue(allChunks.isNotEmpty())

        val categories = allChunks.map { it.category }.distinct()
        assertTrue(categories.contains(KnowledgeCategory.GENERAL_INFORMATION))
        assertTrue(categories.contains(KnowledgeCategory.LOCATION))
        assertTrue(categories.contains(KnowledgeCategory.OPENING_HOURS))
        assertTrue(categories.contains(KnowledgeCategory.SERVICES))
        assertTrue(categories.contains(KnowledgeCategory.CONTACT))
        assertTrue(categories.contains(KnowledgeCategory.MENU))
        assertTrue(categories.contains(KnowledgeCategory.PIZZAS))
        assertTrue(categories.contains(KnowledgeCategory.DEALS))
        assertTrue(categories.contains(KnowledgeCategory.ANNOUNCEMENTS))
    }

    @Test
    fun `test retrieval for Zinger Burger price`() {
        val result = CheeseBiteRetriever.retrieve("What is the price of Zinger Burger?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        assertTrue(result.relevantChunks.isNotEmpty())
        assertTrue(result.relevantChunks.any { it.content.contains("Zinger Burger — Rs. 320") })

        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("What is the price of Zinger Burger?", result)
        assertTrue(answer.contains("320"))
    }

    @Test
    fun `test retrieval for Opening Hours`() {
        val result = CheeseBiteRetriever.retrieve("What are your opening hours?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("What are your opening hours?", result)
        assertTrue(answer.contains("12:00 PM to 01:00 AM"))
    }

    @Test
    fun `test retrieval for Location and Address`() {
        val result = CheeseBiteRetriever.retrieve("Where is Cheese Bites located?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("Where is Cheese Bites located?", result)
        assertTrue(answer.contains("Mandi Throo"))
        assertTrue(answer.contains("Hamza Traders"))
        assertTrue(answer.contains("Zafarwal Road"))
    }

    @Test
    fun `test retrieval for Free Home Delivery service`() {
        val result = CheeseBiteRetriever.retrieve("Do you offer home delivery?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("Do you offer home delivery?", result)
        assertTrue(answer.contains("Free Home Delivery"))
    }

    @Test
    fun `test retrieval for Large Special Bite Pizza price`() {
        val result = CheeseBiteRetriever.retrieve("What is the price of a Large Special Bite Pizza?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("What is the price of a Large Special Bite Pizza?", result)
        assertTrue(answer.contains("1850"))
    }

    @Test
    fun `test retrieval for Student Deal 1`() {
        val result = CheeseBiteRetriever.retrieve("What is Student Deal 1?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("What is Student Deal 1?", result)
        assertTrue(answer.contains("1750"))
        assertTrue(answer.contains("Pan Pizza"))
        assertTrue(answer.contains("2 Zinger Burgers"))
        assertTrue(answer.contains("5 Nuggets"))
        assertTrue(answer.contains("5 Hot Wings"))
    }

    @Test
    fun `test retrieval for BBQ menu coming soon`() {
        val result = CheeseBiteRetriever.retrieve("Do you have BBQ?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("Do you have BBQ?", result)
        assertTrue(answer.contains("Coming Soon"))
        assertTrue(answer.contains("Chicken Tika"))
    }

    @Test
    fun `test retrieval for Chicken Tika price`() {
        val result = CheeseBiteRetriever.retrieve("What is the price of Chicken Tika?")
        assertEquals(QueryIntent.RESTAURANT_FACTUAL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("What is the price of Chicken Tika?", result)
        assertTrue(answer.contains("150"))
    }

    @Test
    fun `test anti-hallucination on unknown restaurant details like ingredients`() {
        val result = CheeseBiteRetriever.retrieve("What ingredients are in the Zinger Burger?")
        assertEquals(QueryIntent.UNKNOWN_RESTAURANT_DETAIL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("What ingredients are in the Zinger Burger?", result)
        // Must politely state that information is not available and redirect to available topics
        assertTrue(answer.contains("don’t have the ingredient details") || answer.contains("does not include"))
        assertFalse(answer.contains("mayonnaise")) // Must NOT hallucinate ingredients!
    }

    @Test
    fun `test anti-hallucination on unknown delivery time inquiry`() {
        val result = CheeseBiteRetriever.retrieve("How long does delivery take?")
        assertEquals(QueryIntent.UNKNOWN_RESTAURANT_DETAIL, result.intent)
        val answer = CheeseBiteLocalGroundedGenerator.generateResponse("How long does delivery take?", result)
        assertTrue(answer.contains("does not specify estimated delivery times") || answer.contains("Free Home Delivery"))
        assertFalse(answer.contains("30 minutes")) // Must NOT guess a delivery time!
    }

    @Test
    fun `test out of scope questions are politely redirected to Cheese Bites`() {
        val queries = listOf(
            "Who is the president of Pakistan?",
            "Tell me a joke.",
            "How do I learn Python?",
            "What is the weather today?",
            "Write me a poem."
        )

        for (q in queries) {
            val result = CheeseBiteRetriever.retrieve(q)
            assertEquals("Expected OUT_OF_SCOPE for: $q", QueryIntent.OUT_OF_SCOPE, result.intent)
            val answer = CheeseBiteLocalGroundedGenerator.generateResponse(q, result)
            assertTrue("Expected redirection for: $q", answer.contains("Cheese Bites"))
        }
    }

    @Test
    fun `test conversational greetings and pleasantries`() {
        val salamResult = CheeseBiteRetriever.retrieve("Assalamualaikum")
        assertEquals(QueryIntent.GREETING, salamResult.intent)
        assertTrue(salamResult.conversationalPrompt?.contains("Wa Alaikum Assalam") == true)

        val hiResult = CheeseBiteRetriever.retrieve("Hi")
        assertEquals(QueryIntent.GREETING, hiResult.intent)
        assertTrue(hiResult.conversationalPrompt?.contains("Welcome to Cheese Bites") == true)

        val thanksResult = CheeseBiteRetriever.retrieve("Thank you")
        assertEquals(QueryIntent.PLEASANTRY, thanksResult.intent)
        assertTrue(thanksResult.conversationalPrompt?.contains("welcome") == true)
    }

    @Test
    fun `test dynamic knowledge base update without rebuilding app`() {
        val originalChunk = CheeseBiteKnowledgeBase.getChunkById("menu_burgers")
        assertNotNull(originalChunk)

        // Simulate updating the burgers menu price dynamically
        val updatedContent = originalChunk!!.content.replace("Zinger Burger — Rs. 320", "Zinger Burger — Rs. 350")
        val success = CheeseBiteKnowledgeBase.updateChunkContent("menu_burgers", updatedContent)
        assertTrue(success)

        val updatedChunk = CheeseBiteKnowledgeBase.getChunkById("menu_burgers")
        assertTrue(updatedChunk!!.content.contains("Zinger Burger — Rs. 350"))

        // Restore original
        CheeseBiteKnowledgeBase.updateChunkContent("menu_burgers", originalChunk.content)
        val restoredChunk = CheeseBiteKnowledgeBase.getChunkById("menu_burgers")
        assertTrue(restoredChunk!!.content.contains("Zinger Burger — Rs. 320"))
    }
}
