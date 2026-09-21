package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CartItem
import com.example.ui.components.EmptyStateView
import com.example.ui.components.FoodItemThumbnail
import com.example.ui.components.PrimaryButton
import com.example.ui.components.QuantitySelector
import com.example.ui.theme.CheeseGoldPrimary
import com.example.ui.theme.CheeseRed
import com.example.ui.theme.HerbGreen
import com.example.viewmodel.CheeseBiteViewModel

@Composable
fun CartScreen(
    viewModel: CheeseBiteViewModel,
    onNavigateToCheckout: () -> Unit,
    onExploreMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var promoInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("cart_screen")
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Food Cart (${uiState.cartItemCount})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (uiState.cartItems.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearCart() }) {
                        Text(
                            text = "Clear All",
                            style = MaterialTheme.typography.labelMedium.copy(color = CheeseRed)
                        )
                    }
                }
            }
        }

        if (uiState.cartItems.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.RemoveShoppingCart,
                title = "Your Cart is Empty",
                description = "Looks like you haven't added any cheesy bites yet! Check out our mouth-watering pizza deals and zinger burgers.",
                actionLabel = "Browse Menu Deals",
                onActionClick = onExploreMenuClick,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cart Items
                items(uiState.cartItems, key = { it.cartItemId }) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onIncrement = { viewModel.updateCartItemQuantity(cartItem.cartItemId, 1) },
                        onDecrement = { viewModel.updateCartItemQuantity(cartItem.cartItemId, -1) },
                        onRemove = { viewModel.removeCartItem(cartItem.cartItemId) }
                    )
                }

                // Promo Code Section
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = CheeseGoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Have a Coupon Code?",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (uiState.appliedPromoCode != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(HerbGreen.copy(alpha = 0.12f))
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = HerbGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "'${uiState.appliedPromoCode}' applied (-${uiState.promoDiscountPercent.toInt()}%)",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = HerbGreen
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.removePromoCode() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove coupon",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = promoInput,
                                        onValueChange = { promoInput = it },
                                        placeholder = {
                                            Text(
                                                text = "e.g. CHEESE10",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        },
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = CheeseGoldPrimary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("promo_code_input")
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(CheeseGoldPrimary)
                                            .clickable {
                                                if (viewModel.applyPromoCode(promoInput)) {
                                                    promoInput = ""
                                                }
                                            }
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "APPLY",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tip: Use code 'CHEESE10' for 10% discount!",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Bill Breakdown
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Items Subtotal",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Rs. ${uiState.subtotal.toInt()}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Delivery Fee (Free Promo)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "FREE",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = HerbGreen
                                    )
                                )
                            }

                            if (uiState.discount > 0.0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Promo Discount (${uiState.appliedPromoCode})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = HerbGreen
                                    )
                                    Text(
                                        text = "- Rs. ${uiState.discount.toInt()}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = HerbGreen
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total to Pay",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Rs. ${uiState.finalTotal.toInt()}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CheeseGoldPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Proceed Button
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    PrimaryButton(
                        text = "Proceed to Checkout (Rs. ${uiState.finalTotal.toInt()})",
                        onClick = onNavigateToCheckout,
                        leadingIcon = Icons.Default.ShoppingCart,
                        modifier = Modifier.testTag("checkout_button")
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodItemThumbnail(
                foodItem = cartItem.foodItem,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.foodItem.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (cartItem.selectedSize != null) {
                    Text(
                        text = "Size: ${cartItem.selectedSize.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (cartItem.selectedAddOns.isNotEmpty()) {
                    Text(
                        text = "+ " + cartItem.selectedAddOns.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (cartItem.specialInstructions.isNotBlank()) {
                    Text(
                        text = "Note: ${cartItem.specialInstructions}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = CheeseGoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Rs. ${cartItem.totalPrice.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = CheeseGoldPrimary
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                QuantitySelector(
                    quantity = cartItem.quantity,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement,
                    size = 28
                )
            }
        }
    }
}
