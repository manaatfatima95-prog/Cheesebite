package com.example.chatbot.model

import java.util.UUID

enum class KnowledgeCategory(val displayName: String) {
    GENERAL_INFORMATION("General Information"),
    LOCATION("Location"),
    OPENING_HOURS("Opening Hours"),
    CONTACT("Contact Numbers"),
    SERVICES("Services"),
    ANNOUNCEMENTS("Announcements"),
    MENU("Menu Items"),
    PIZZAS("Pizzas"),
    DEALS("Deals & Packages")
}

data class KnowledgeChunk(
    val id: String,
    val title: String,
    val category: KnowledgeCategory,
    val content: String,
    val keywords: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val retrievedChunkIds: List<String> = emptyList(),
    val isError: Boolean = false
)

enum class QueryIntent {
    GREETING,
    PLEASANTRY,
    OUT_OF_SCOPE,
    UNKNOWN_RESTAURANT_DETAIL,
    RESTAURANT_FACTUAL
}

data class RetrievalResult(
    val intent: QueryIntent,
    val relevantChunks: List<KnowledgeChunk>,
    val unknownDetailType: String? = null,
    val conversationalPrompt: String? = null
)
