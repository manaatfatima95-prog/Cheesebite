package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.ui.components.HorizontalFoodCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.VerticalFoodCard
import com.example.ui.components.branding.CheeseBiteSymbol
import com.example.ui.theme.BrandCharcoalDark
import com.example.ui.theme.CheeseGoldDark
import com.example.ui.theme.CheeseGoldPrimary
import com.example.ui.theme.CheeseRed
import com.example.ui.theme.HerbGreen
import com.example.viewmodel.CheeseBiteViewModel

@Composable
fun HomeScreen(
    viewModel: CheeseBiteViewModel,
    onNavigateToFoodDetail: (FoodItem) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToRestaurant: () -> Unit,
    onNavigateToChatbot: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val featuredDeals = uiState.allFoodItems.filter { it.category == "deals" }
    val popularItems = uiState.allFoodItems.filter { it.isBestseller }
    val categoryItems = viewModel.getItemsForCategory(uiState.selectedCategoryId)
    val isCategoryFiltered = uiState.selectedCategoryId != "all"

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_screen_content"),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
        // Search trigger bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onNavigateToSearch() }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search pizza, zinger burger, loaded fries...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Hero Promotional Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigateToRestaurant() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_promo_deal),
                        contentDescription = "Cheese Bite Promotion",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Content on Banner
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CheeseRed)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "FREE HOME DELIVERY",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CheeseGoldPrimary)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "12 PM - 01 AM",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BrandCharcoalDark)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CheeseBiteSymbol(
                                    size = 26.dp,
                                    badgeShape = false
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Chesee Bite Resturent",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 21.sp
                                ),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Mandi Throo, Near Hamza Traders Zafarwal Road",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Restaurant Quick Contact Strip
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(HerbGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeliveryDining,
                                contentDescription = null,
                                tint = HerbGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Free Delivery • Min Rs. 0",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Hot & Cheesy Goodness",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(CheeseGoldPrimary.copy(alpha = 0.15f))
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:03209163877")
                                }
                                context.startActivity(intent)
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Restaurant",
                                tint = CheeseGoldDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "0320-9163877",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = CheeseGoldDark
                            )
                        }
                    }
                }
            }
        }

        // Cheese Bites AI Assistant Feature Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToChatbot() }
                    .testTag("ai_assistant_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(CheeseGoldPrimary, CheeseGoldDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ask Cheese Bites AI",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Questions about deals, menu prices, or hours? Chat now!",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Chat",
                        tint = CheeseGoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Categories Scroll
        item {
            SectionHeader(
                title = "Categories",
                actionText = "See All",
                onActionClick = onNavigateToCategories
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    SimpleCategoryChip(
                        name = "All",
                        iconEmoji = "📋",
                        isSelected = !isCategoryFiltered,
                        onClick = { viewModel.selectCategory("all") }
                    )
                }

                items(uiState.categories) { category ->
                    val isSelected = category.id == uiState.selectedCategoryId
                    CategoryChip(
                        category = category,
                        isSelected = isSelected,
                        onClick = { viewModel.selectCategory(category.id) }
                    )
                }
            }
        }

        if (!isCategoryFiltered) {
            // Featured Deals Section (Horizontal list)
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SectionHeader(
                    title = "Special Pizza & Burger Deals",
                    subtitle = "Handpicked combos with maximum savings"
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(featuredDeals) { deal ->
                        VerticalFoodCard(
                            foodItem = deal,
                            isFavorite = viewModel.isFavorite(deal.id),
                            onItemClick = { onNavigateToFoodDetail(deal) },
                            onAddToCartClick = { viewModel.addToCart(deal) },
                            onFavoriteToggle = { viewModel.toggleFavorite(deal.id) }
                        )
                    }
                }
            }

            // Popular Items Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Most Popular at Cheese Bite",
                    subtitle = "Customer favorites in Zafarwal & Mandi Throo"
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(popularItems) { item ->
                        VerticalFoodCard(
                            foodItem = item,
                            isFavorite = viewModel.isFavorite(item.id),
                            onItemClick = { onNavigateToFoodDetail(item) },
                            onAddToCartClick = { viewModel.addToCart(item) },
                            onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                        )
                    }
                }
            }
        } else {
            // FILTERED CATEGORY VIEW: ONLY RELEVANT PRODUCTS (NO UNRELATED SECTIONS MIXED IN)
            if (uiState.selectedCategoryId == "pizza") {
                val specialPizzas = viewModel.getSpecialPizzas()
                val regularPizzas = viewModel.getRegularPizzas()

                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    SectionHeader(
                        title = "Special Pizza",
                        subtitle = "Gourmet crust with loaded premium toppings"
                    )
                }

                items(specialPizzas) { item ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        HorizontalFoodCard(
                            foodItem = item,
                            isFavorite = viewModel.isFavorite(item.id),
                            onItemClick = { onNavigateToFoodDetail(item) },
                            onAddToCartClick = { viewModel.addToCart(item) },
                            onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    SectionHeader(
                        title = "Regular Pizza",
                        subtitle = "Classic favorites baked fresh to perfection"
                    )
                }

                items(regularPizzas) { item ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        HorizontalFoodCard(
                            foodItem = item,
                            isFavorite = viewModel.isFavorite(item.id),
                            onItemClick = { onNavigateToFoodDetail(item) },
                            onAddToCartClick = { viewModel.addToCart(item) },
                            onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                        )
                    }
                }
            } else if (uiState.selectedCategoryId == "deals") {
                val studentDeals = viewModel.getStudentDeals()
                val pizzaDeals = viewModel.getPizzaDeals()
                val burgerDeals = viewModel.getBurgerDeals()

                if (studentDeals.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        SectionHeader(
                            title = "Student Deals",
                            subtitle = "Value packed deals for students & friends"
                        )
                    }
                    items(studentDeals) { item ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }
                    }
                }

                if (pizzaDeals.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        SectionHeader(
                            title = "Pizza Deals",
                            subtitle = "Mega pizza combo feasts with cold drinks & sides"
                        )
                    }
                    items(pizzaDeals) { item ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }
                    }
                }

                if (burgerDeals.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        SectionHeader(
                            title = "Burger Deals",
                            subtitle = "Zinger & patty combos with sides & cold drinks"
                        )
                    }
                    items(burgerDeals) { item ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }
                    }
                }
            } else {
                val selectedCategoryName = uiState.categories.find { it.id == uiState.selectedCategoryId }?.name ?: "Products"
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    SectionHeader(
                        title = selectedCategoryName,
                        subtitle = "${categoryItems.size} items available"
                    )
                }

                if (categoryItems.isEmpty()) {
                    item {
                        Box(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "No items available in this category.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(categoryItems) { item ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Floating Action Button to launch AI Assistant
    FloatingActionButton(
        onClick = onNavigateToChatbot,
        containerColor = CheeseGoldPrimary,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 100.dp)
            .testTag("chatbot_fab")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = "Ask Cheese Bites AI",
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Ask AI",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
}

@Composable
fun CategoryChip(
    category: FoodCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) CheeseGoldPrimary else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("category_chip_${category.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.iconEmoji,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                ),
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SimpleCategoryChip(
    name: String,
    iconEmoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) CheeseGoldPrimary else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("category_chip_${name.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = iconEmoji,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                ),
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
