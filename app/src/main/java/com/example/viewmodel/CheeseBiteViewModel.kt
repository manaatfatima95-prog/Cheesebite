package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CheeseBiteRepository
import com.example.model.AppNotification
import com.example.model.CartItem
import com.example.model.DeliveryAddress
import com.example.model.FoodAddOn
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.model.FoodSize
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class CheeseBiteUiState(
    val categories: List<FoodCategory> = CheeseBiteRepository.categories,
    val allFoodItems: List<FoodItem> = CheeseBiteRepository.foodItems,
    val selectedCategoryId: String = "deals",
    val searchQuery: String = "",
    val searchResults: List<FoodItem> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val appliedPromoCode: String? = null,
    val promoDiscountPercent: Double = 0.0,
    val favoriteItemIds: Set<String> = setOf("deal_pizza_02", "bur_zinger", "sp_cheese_bite"),
    val activeOrder: Order? = null,
    val pastOrders: List<Order> = emptyList(),
    val addresses: List<DeliveryAddress> = CheeseBiteRepository.defaultAddresses,
    val selectedAddress: DeliveryAddress = CheeseBiteRepository.defaultAddresses.first(),
    val userProfile: UserProfile = CheeseBiteRepository.defaultProfile,
    val isDarkTheme: Boolean = true,
    val notifications: List<AppNotification> = CheeseBiteRepository.initialNotifications,
    val isOrderProcessing: Boolean = false,
    val selectedFoodItem: FoodItem? = null
) {
    val cartItemCount: Int
        get() = cartItems.sumOf { it.quantity }

    val subtotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val discount: Double
        get() = if (appliedPromoCode != null && promoDiscountPercent > 0.0) {
            subtotal * (promoDiscountPercent / 100.0)
        } else {
            0.0
        }

    val deliveryFee: Double = 0.0 // Free Home Delivery in Mandi Throo & Zafarwal Rd

    val finalTotal: Double
        get() = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)
}

class CheeseBiteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheeseBiteUiState())
    val uiState: StateFlow<CheeseBiteUiState> = _uiState.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    init {
        // Pre-populate with a demo initial order so order tracking / history is ready to demo right away
        val sampleCartItem = CartItem(
            cartItemId = "init_item_1",
            foodItem = CheeseBiteRepository.foodItems.first { it.id == "deal_pizza_05" },
            quantity = 1
        )
        val initialOrder = Order(
            orderId = "CB-8924",
            items = listOf(sampleCartItem),
            subtotal = 1600.0,
            deliveryFee = 0.0,
            discount = 0.0,
            total = 1600.0,
            status = OrderStatus.PREPARING,
            deliveryAddress = "House 14, Street 2, Mandi Throo",
            paymentMethod = "Cash on Delivery",
            note = "Please deliver hot, near Hamza Traders"
        )
        _uiState.update {
            it.copy(
                activeOrder = initialOrder,
                pastOrders = listOf(
                    Order(
                        orderId = "CB-7612",
                        items = listOf(
                            CartItem(
                                cartItemId = "past_item_1",
                                foodItem = CheeseBiteRepository.foodItems.first { item -> item.id == "deal_burger_01" },
                                quantity = 1
                            )
                        ),
                        subtotal = 1000.0,
                        deliveryFee = 0.0,
                        discount = 0.0,
                        total = 1000.0,
                        status = OrderStatus.DELIVERED,
                        deliveryAddress = "Shop 5, Main Commercial Market, Zafarwal Road",
                        paymentMethod = "Cash on Delivery"
                    )
                )
            )
        }
    }

    fun selectCategory(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val trimmed = query.trim()
            val filtered = if (trimmed.isEmpty()) {
                emptyList()
            } else {
                state.allFoodItems.filter { item ->
                    item.name.contains(trimmed, ignoreCase = true) ||
                            item.description.contains(trimmed, ignoreCase = true) ||
                            item.category.contains(trimmed, ignoreCase = true)
                }
            }
            state.copy(searchQuery = query, searchResults = filtered)
        }
    }

    fun selectFoodItem(item: FoodItem) {
        _uiState.update { it.copy(selectedFoodItem = item) }
    }

    fun toggleFavorite(itemId: String) {
        _uiState.update { state ->
            val updated = state.favoriteItemIds.toMutableSet()
            val isNowFav = if (updated.contains(itemId)) {
                updated.remove(itemId)
                false
            } else {
                updated.add(itemId)
                true
            }
            state.copy(favoriteItemIds = updated)
        }
        viewModelScope.launch {
            _snackbarMessages.emit("Favorites updated")
        }
    }

    fun isFavorite(itemId: String): Boolean {
        return _uiState.value.favoriteItemIds.contains(itemId)
    }

    fun addToCart(
        foodItem: FoodItem,
        selectedSize: FoodSize? = null,
        selectedAddOns: List<FoodAddOn> = emptyList(),
        specialInstructions: String = "",
        quantity: Int = 1
    ) {
        val cartItemId = UUID.randomUUID().toString()
        val newCartItem = CartItem(
            cartItemId = cartItemId,
            foodItem = foodItem,
            selectedSize = selectedSize ?: foodItem.sizes.firstOrNull(),
            selectedAddOns = selectedAddOns,
            specialInstructions = specialInstructions,
            quantity = quantity
        )

        _uiState.update { state ->
            // Check if same configuration already in cart
            val existingIndex = state.cartItems.indexOfFirst {
                it.foodItem.id == foodItem.id &&
                        it.selectedSize == newCartItem.selectedSize &&
                        it.selectedAddOns == newCartItem.selectedAddOns &&
                        it.specialInstructions == newCartItem.specialInstructions
            }

            val updatedCart = if (existingIndex != -1) {
                state.cartItems.toMutableList().also { list ->
                    val existing = list[existingIndex]
                    list[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
                }
            } else {
                state.cartItems + newCartItem
            }

            state.copy(cartItems = updatedCart)
        }

        viewModelScope.launch {
            _snackbarMessages.emit("Added ${foodItem.name} to Cart")
        }
    }

    fun updateCartItemQuantity(cartItemId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.cartItemId == cartItemId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else {
                    item
                }
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeCartItem(cartItemId: String) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems.filterNot { it.cartItemId == cartItemId })
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList(), appliedPromoCode = null, promoDiscountPercent = 0.0) }
    }

    fun applyPromoCode(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        return when (trimmed) {
            "CHEESE10" -> {
                _uiState.update { it.copy(appliedPromoCode = "CHEESE10", promoDiscountPercent = 10.0) }
                viewModelScope.launch { _snackbarMessages.emit("Promo code CHEESE10 applied! (10% OFF)") }
                true
            }
            "WELCOME15" -> {
                _uiState.update { it.copy(appliedPromoCode = "WELCOME15", promoDiscountPercent = 15.0) }
                viewModelScope.launch { _snackbarMessages.emit("Promo code WELCOME15 applied! (15% OFF)") }
                true
            }
            else -> {
                viewModelScope.launch { _snackbarMessages.emit("Invalid promo code. Try 'CHEESE10'") }
                false
            }
        }
    }

    fun removePromoCode() {
        _uiState.update { it.copy(appliedPromoCode = null, promoDiscountPercent = 0.0) }
    }

    fun selectAddress(address: DeliveryAddress) {
        _uiState.update { it.copy(selectedAddress = address) }
    }

    fun addNewAddress(label: String, fullAddress: String, landmark: String, phone: String) {
        val newAddr = DeliveryAddress(
            id = UUID.randomUUID().toString(),
            label = label,
            fullAddress = fullAddress,
            landmark = landmark,
            phone = phone,
            isDefault = false
        )
        _uiState.update { state ->
            state.copy(
                addresses = state.addresses + newAddr,
                selectedAddress = newAddr
            )
        }
        viewModelScope.launch { _snackbarMessages.emit("Address saved!") }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun placeOrder(paymentMethod: String, note: String, onOrderPlaced: (String) -> Unit) {
        val currentState = _uiState.value
        if (currentState.cartItems.isEmpty()) return

        _uiState.update { it.copy(isOrderProcessing = true) }

        viewModelScope.launch {
            delay(1200) // Realistic network delay
            val randomNum = (1000..9999).random()
            val orderId = "CB-$randomNum"
            val newOrder = Order(
                orderId = orderId,
                items = currentState.cartItems,
                subtotal = currentState.subtotal,
                deliveryFee = currentState.deliveryFee,
                discount = currentState.discount,
                total = currentState.finalTotal,
                status = OrderStatus.PLACED,
                deliveryAddress = currentState.selectedAddress.fullAddress,
                paymentMethod = paymentMethod,
                note = note
            )

            _uiState.update {
                it.copy(
                    isOrderProcessing = false,
                    cartItems = emptyList(),
                    appliedPromoCode = null,
                    promoDiscountPercent = 0.0,
                    activeOrder = newOrder,
                    pastOrders = if (it.activeOrder != null) listOf(it.activeOrder) + it.pastOrders else it.pastOrders
                )
            }

            _snackbarMessages.emit("Order #$orderId placed successfully!")
            onOrderPlaced(orderId)
        }
    }

    fun advanceOrderStatus() {
        val current = _uiState.value.activeOrder ?: return
        val nextStatus = when (current.status) {
            OrderStatus.PLACED -> OrderStatus.ACCEPTED
            OrderStatus.ACCEPTED -> OrderStatus.PREPARING
            OrderStatus.PREPARING -> OrderStatus.READY
            OrderStatus.READY -> OrderStatus.OUT_FOR_DELIVERY
            OrderStatus.OUT_FOR_DELIVERY -> OrderStatus.DELIVERED
            OrderStatus.DELIVERED -> OrderStatus.DELIVERED
        }

        val updatedOrder = current.copy(status = nextStatus)
        _uiState.update { state ->
            state.copy(
                activeOrder = if (nextStatus == OrderStatus.DELIVERED) null else updatedOrder,
                pastOrders = if (nextStatus == OrderStatus.DELIVERED) {
                    listOf(updatedOrder) + state.pastOrders.filterNot { it.orderId == current.orderId }
                } else state.pastOrders
            )
        }

        viewModelScope.launch {
            _snackbarMessages.emit("Order Status updated to: ${nextStatus.display}")
        }
    }

    fun reorder(order: Order) {
        _uiState.update { state ->
            state.copy(cartItems = order.items)
        }
        viewModelScope.launch {
            _snackbarMessages.emit("Items from #${order.orderId} added to Cart!")
        }
    }

    fun updateProfile(name: String, phone: String, email: String) {
        _uiState.update {
            it.copy(userProfile = UserProfile(name = name, phone = phone, email = email))
        }
        viewModelScope.launch {
            _snackbarMessages.emit("Profile updated successfully")
        }
    }

    fun markNotificationRead(id: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }
            )
        }
    }

    fun clearNotifications() {
        _uiState.update { it.copy(notifications = emptyList()) }
    }
}
