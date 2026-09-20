package com.example.model

data class FoodSize(
    val name: String, // e.g. "Pan 8\"", "Small 10\"", "Med 12\"", "Large 14\"", "XL 16\""
    val price: Double
)

data class FoodAddOn(
    val id: String,
    val name: String,
    val price: Double
)

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val basePrice: Double,
    val sizes: List<FoodSize> = emptyList(),
    val availableAddOns: List<FoodAddOn> = emptyList(),
    val imageRes: Int? = null,
    val isBestseller: Boolean = false,
    val rating: Double = 4.8,
    val reviewCount: Int = 38,
    val isComingSoon: Boolean = false
) {
    fun displayPrice(): String {
        return if (sizes.isNotEmpty()) {
            "From Rs. ${basePrice.toInt()}"
        } else {
            "Rs. ${basePrice.toInt()}"
        }
    }
}

data class CartItem(
    val cartItemId: String,
    val foodItem: FoodItem,
    val selectedSize: FoodSize? = null,
    val selectedAddOns: List<FoodAddOn> = emptyList(),
    val specialInstructions: String = "",
    val quantity: Int = 1
) {
    val unitPrice: Double
        get() {
            val base = selectedSize?.price ?: foodItem.basePrice
            val addOnsSum = selectedAddOns.sumOf { it.price }
            return base + addOnsSum
        }

    val totalPrice: Double
        get() = unitPrice * quantity
}

data class FoodCategory(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val description: String = ""
)

enum class OrderStatus(val display: String, val subtitle: String) {
    PLACED("Order Placed", "Your order has been received by Cheese Bite"),
    ACCEPTED("Order Accepted", "Restaurant confirmed your order"),
    PREPARING("Preparing", "Fresh food is being prepared in the kitchen"),
    READY("Ready for Pickup", "Order packed and handed to delivery rider"),
    OUT_FOR_DELIVERY("Out for Delivery", "Rider is on the way to your location"),
    DELIVERED("Delivered", "Enjoy your delicious Cheese Bite meal!")
}

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double = 0.0, // Free Home Delivery
    val discount: Double = 0.0,
    val total: Double,
    val status: OrderStatus = OrderStatus.PLACED,
    val timestamp: Long = System.currentTimeMillis(),
    val deliveryAddress: String,
    val paymentMethod: String,
    val note: String = "",
    val riderName: String = "Rider: Tariq Mahmood",
    val restaurantPhone: String = "0320-9163877"
)

data class DeliveryAddress(
    val id: String,
    val label: String, // "Home", "Office", "Shop"
    val fullAddress: String,
    val landmark: String = "",
    val phone: String,
    val isDefault: Boolean = false
)

data class UserProfile(
    val name: String,
    val phone: String,
    val email: String
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false
)
