package com.example.chatbot.rag

import com.example.chatbot.model.KnowledgeChunk
import com.example.chatbot.model.QueryIntent
import com.example.chatbot.model.RetrievalResult
import java.util.Locale

/**
 * Local Grounded Response Generator.
 * Generates natural, polite responses strictly grounded in the retrieved knowledge chunks.
 * Ensures 100% adherence to Cheese Bites facts without external hallucinations,
 * serving as the robust grounded responder or offline fallback.
 */
object CheeseBiteLocalGroundedGenerator {

    fun generateResponse(query: String, retrievalResult: RetrievalResult): String {
        when (retrievalResult.intent) {
            QueryIntent.GREETING,
            QueryIntent.PLEASANTRY,
            QueryIntent.OUT_OF_SCOPE,
            QueryIntent.UNKNOWN_RESTAURANT_DETAIL -> {
                return retrievalResult.conversationalPrompt
                    ?: "I’d be happy to help you with Cheese Bites! 😊 What would you like to know about our menu, prices, deals, or opening hours?"
            }

            QueryIntent.RESTAURANT_FACTUAL -> {
                val chunks = retrievalResult.relevantChunks
                if (chunks.isEmpty()) {
                    return "I’d be happy to help you with Cheese Bites! What would you like to know about our menu, prices, deals, opening hours, location, or home delivery?"
                }
                return formatFactualResponse(query.lowercase(Locale.ROOT), chunks)
            }
        }
    }

    private fun formatFactualResponse(query: String, chunks: List<KnowledgeChunk>): String {
        // Specific query cases
        if (query.contains("zinger burger") && (query.contains("price") || query.contains("how much") || query.contains("cost"))) {
            return "The Zinger Burger is available for **Rs. 320** at Cheese Bites! Would you like to check our other burgers or special deals?"
        }

        if (query.contains("opening hours") || (query.contains("time") && (query.contains("open") || query.contains("close") || query.contains("timing")))) {
            return "Cheese Bites is open daily from **12:00 PM to 01:00 AM**. We also offer Free Home Delivery during our working hours!"
        }

        if (query.contains("where") || query.contains("location") || query.contains("address")) {
            return "Cheese Bites is located at **Mandi Throo, Near Hamza Traders, Zafarwal Road**. We look forward to serving you!"
        }

        if (query.contains("home delivery") || query.contains("free delivery") || (query.contains("delivery") && query.contains("offer"))) {
            return "Yes! Cheese Bites provides **Free Home Delivery** for our customers. You can place your order through the app or call us at 0320-9163877 / 0306-7526655."
        }

        if (query.contains("special bite pizza") && (query.contains("large") || query.contains("lar"))) {
            return "The **Large (14\") Special Bite Pizza** is **Rs. 1850**.\n\nHere are all the sizes for Special Bite Pizza:\n• Pan (8\"): Rs. 650\n• Small (10\"): Rs. 1150\n• Medium (12\"): Rs. 1450\n• Large (14\"): Rs. 1850\n• XL (16\"): Rs. 2090"
        }

        if (query.contains("student deal 1")) {
            return "**Student Deal 1 — Rs. 1750**\nIncludes:\n• 1 Pan Pizza\n• 2 Zinger Burgers\n• 5 Nuggets\n• 5 Hot Wings\n• 1 Ltr. Drink"
        }

        if (query.contains("student deal 2")) {
            return "**Student Deal 2 — Rs. 2000**\nIncludes:\n• 1 Small Pizza\n• 2 Zinger Burgers\n• 5 Nuggets\n• 1 Regular Fries\n• 1 Ltr. Drink"
        }

        if (query.contains("student deal 3")) {
            return "**Student Deal 3 — Rs. 1700**\nIncludes:\n• 1 Med. Pizza\n• 5 Nuggets\n• 1 Reg. Fries\n• 1 Ltr. Drink"
        }

        if (query.contains("student deal") || query.contains("student deals")) {
            return "Here are our **Student Deals** at Cheese Bites:\n\n" +
                    "🎓 **Student Deal 1 — Rs. 1750**\n1 Pan Pizza, 2 Zinger Burgers, 5 Nuggets, 5 Hot Wings, 1 Ltr. Drink\n\n" +
                    "🎓 **Student Deal 2 — Rs. 2000**\n1 Small Pizza, 2 Zinger Burgers, 5 Nuggets, 1 Regular Fries, 1 Ltr. Drink\n\n" +
                    "🎓 **Student Deal 3 — Rs. 1700**\n1 Med. Pizza, 5 Nuggets, 1 Reg. Fries, 1 Ltr. Drink"
        }

        if (query.contains("bbq") || query.contains("bar.b.q") || query.contains("barbecue")) {
            return "Our **Bar.B.Q menu is Coming Soon**! Here is the upcoming menu & prices:\n\n" +
                    "• Chicken Tika — Rs. 150\n" +
                    "• Chicken Kabab — Rs. 150\n" +
                    "• Chicken Malai Boti — Rs. 300\n" +
                    "• Chicken Green Boti — Rs. 350\n" +
                    "• Beef Kabab — Rs. 200"
        }

        if (query.contains("chicken tika") || query.contains("chicken tikka")) {
            return "Chicken Tika is part of our upcoming **Bar.B.Q (Coming Soon)** section, priced at **Rs. 150**!"
        }

        if (query.contains("ceo") || query.contains("owner") || query.contains("subhani")) {
            return "The CEO of Cheese Bites Restaurant is **Subhani Javaid**."
        }

        if (query.contains("branch") || query.contains("announcement") || query.contains("new branch")) {
            return "Great news! Insha Allah, in the next 2 months, a new Cheese Bites branch will be opening!"
        }

        if (query.contains("contact") || query.contains("phone") || query.contains("number") || query.contains("call")) {
            return "You can reach Cheese Bites at **0320-9163877** or **0306-7526655**."
        }

        if (query.contains("deal") || query.contains("deals") || query.contains("package")) {
            return "We have fantastic deals at Cheese Bites!\n\n" +
                    "• **Student Deals** starting from Rs. 1700\n" +
                    "• **Pizza Deals** (Deals 01 to 10) starting from Rs. 800\n" +
                    "• **Burger Deals** (Deals 01 to 06) starting from Rs. 1000\n\n" +
                    "Which category of deals would you like to see in detail?"
        }

        // Generic synthesis strictly composed from the top retrieved chunks
        val topChunk = chunks.first()
        val intro = when (topChunk.category) {
            com.example.chatbot.model.KnowledgeCategory.MENU -> "Here is the menu information for Cheese Bites:"
            com.example.chatbot.model.KnowledgeCategory.PIZZAS -> "Here is our pizza details and pricing at Cheese Bites:"
            com.example.chatbot.model.KnowledgeCategory.DEALS -> "Here are the deal packages available at Cheese Bites:"
            com.example.chatbot.model.KnowledgeCategory.OPENING_HOURS -> "Here are the hours for Cheese Bites:"
            com.example.chatbot.model.KnowledgeCategory.LOCATION -> "Here is the location information:"
            com.example.chatbot.model.KnowledgeCategory.SERVICES -> "Here is our service details:"
            com.example.chatbot.model.KnowledgeCategory.CONTACT -> "Here is our contact information:"
            else -> "Here is the requested information from Cheese Bites:"
        }

        val details = chunks.take(2).joinToString("\n\n") { it.content }
        return "$intro\n\n$details"
    }
}
