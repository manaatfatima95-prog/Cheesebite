package com.example.chatbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.model.ChatMessage
import com.example.chatbot.rag.CheeseBiteRetriever
import com.example.chatbot.rag.GeminiRAGService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatbotUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ChatbotViewModel(
    private val ragService: GeminiRAGService = GeminiRAGService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatbotUiState(
            messages = listOf(
                ChatMessage(
                    text = "Assalamualaikum! 👋 Welcome to Cheese Bites. I’m here to help you with our menu, prices, deals, opening hours, location, and home delivery information. What would you like to know?",
                    isUser = false
                )
            )
        )
    )
    val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()

    fun sendMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank() || _uiState.value.isLoading) return

        val userMessage = ChatMessage(text = trimmed, isUser = true)
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                // RAG Step 1 & 2: Query Processing & Knowledge Retrieval
                val retrievalResult = CheeseBiteRetriever.retrieve(trimmed)

                // RAG Step 3 & 4: Relevant Information & Grounded AI Response Generation
                val botResponse = ragService.generateGroundedResponse(trimmed, retrievalResult)

                val botMessage = ChatMessage(
                    text = botResponse,
                    isUser = false,
                    retrievedChunkIds = retrievalResult.relevantChunks.map { it.id }
                )

                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + botMessage,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to process request right now. Please check your connection."
                )
            }
        }
    }

    fun clearChat() {
        _uiState.value = ChatbotUiState(
            messages = listOf(
                ChatMessage(
                    text = "Assalamualaikum! 👋 Welcome to Cheese Bites. I’m here to help you with our menu, prices, deals, opening hours, location, and home delivery information. What would you like to know?",
                    isUser = false
                )
            )
        )
    }
}
