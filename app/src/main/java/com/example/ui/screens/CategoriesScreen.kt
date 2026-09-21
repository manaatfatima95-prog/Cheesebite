package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.ui.components.EmptyStateView
import com.example.ui.components.HorizontalFoodCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CheeseGoldDark
import com.example.ui.theme.CheeseGoldPrimary
import com.example.ui.theme.CheeseRed
import com.example.viewmodel.CheeseBiteViewModel

@Composable
fun CategoriesScreen(
    viewModel: CheeseBiteViewModel,
    onCategorySelected: (String) -> Unit = {},
    onNavigateToFoodDetail: (FoodItem) -> Unit = {},
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isAllCategoriesView = uiState.selectedCategoryId == "all"
    val selectedCategory = uiState.categories.find { it.id == uiState.selectedCategoryId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("categories_screen")
    ) {
        // Top Navigation Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isAllCategoriesView) {
                            onBackClick()
                        } else {
                            viewModel.selectCategory("all")
                            onCategorySelected("all")
                        }
                    },
                    modifier = Modifier.testTag("menu_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAllCategoriesView) "Menu" else (selectedCategory?.name ?: "Category"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isAllCategoriesView) "Explore all food categories & deals" else (selectedCategory?.description ?: "Filtered items"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                if (!isAllCategoriesView) {
                    TextButton(
                        onClick = {
                            viewModel.selectCategory("all")
                            onCategorySelected("all")
                        }
                    ) {
                        Text(
                            text = "All Categories",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = CheeseGoldPrimary
                        )
                    }
                }
            }
        }

        // Horizontal Category Tabs for Fast Switching
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // "All" Tab
            item {
                MenuCategoryChip(
                    label = "All Categories",
                    icon = "📋",
                    isSelected = isAllCategoriesView,
                    onClick = {
                        viewModel.selectCategory("all")
                        onCategorySelected("all")
                    }
                )
            }

            // Normal Product Categories (excluding Deals in standard list)
            val regularCategories = uiState.categories.filter { it.id != "deals" }
            items(regularCategories) { category ->
                val isSelected = category.id == uiState.selectedCategoryId
                MenuCategoryChip(
                    label = category.name,
                    icon = category.iconEmoji,
                    isSelected = isSelected,
                    onClick = {
                        viewModel.selectCategory(category.id)
                        onCategorySelected(category.id)
                    }
                )
            }

            // Distinct Deals Tab at the end of the selector
            val dealsCategory = uiState.categories.find { it.id == "deals" }
            if (dealsCategory != null) {
                val isSelected = uiState.selectedCategoryId == "deals"
                item {
                    MenuCategoryChip(
                        label = "🔥 Deals",
                        icon = "",
                        isSelected = isSelected,
                        isSpecialDeals = true,
                        onClick = {
                            viewModel.selectCategory("deals")
                            onCategorySelected("deals")
                        }
                    )
                }
            }
        }

        // Screen Body: Overview OR Filtered Category View
        if (isAllCategoriesView) {
            // ALL CATEGORIES OVERVIEW
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp, top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Distinct Deals Hero Section
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 1.5.dp,
                                color = CheeseGoldPrimary.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                viewModel.selectCategory("deals")
                                onCategorySelected("deals")
                            }
                            .testTag("deals_section_card")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            CheeseGoldPrimary.copy(alpha = 0.25f),
                                            CheeseRed.copy(alpha = 0.15f)
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(CheeseRed.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = CheeseRed,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Special Deals & Combos",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Student Deals, Pizza Combos & Burger Feasts",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Go to Deals",
                                    tint = CheeseGoldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                // Grid of Food Categories
                item {
                    SectionHeader(
                        title = "Food Categories",
                        subtitle = "Select a category to view its products"
                    )
                }

                val gridCategories = uiState.categories.filter { it.id != "deals" }
                val rows = gridCategories.chunked(2)
                items(rows) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (category in rowItems) {
                            val itemCount = viewModel.getItemsForCategory(category.id).size
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        viewModel.selectCategory(category.id)
                                        onCategorySelected(category.id)
                                    }
                                    .testTag("category_card_${category.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(CheeseGoldPrimary.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = category.iconEmoji, fontSize = 26.sp)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "$itemCount items",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            // FILTERED CATEGORY VIEW (ONLY RELEVANT PRODUCTS)
            when (uiState.selectedCategoryId) {
                "pizza" -> {
                    // PIZZA: Special Pizza and Regular Pizza sections
                    val specialPizzas = viewModel.getSpecialPizzas()
                    val regularPizzas = viewModel.getRegularPizzas()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            SectionHeader(
                                title = "Special Pizza",
                                subtitle = "Gourmet crust with loaded premium toppings"
                            )
                        }

                        items(specialPizzas) { item ->
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            SectionHeader(
                                title = "Regular Pizza",
                                subtitle = "Classic favorites baked fresh to perfection"
                            )
                        }

                        items(regularPizzas) { item ->
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(90.dp)) }
                    }
                }

                "deals" -> {
                    // DEALS ONLY: Student Deals, Pizza Deals, Burger Deals
                    val studentDeals = viewModel.getStudentDeals()
                    val pizzaDeals = viewModel.getPizzaDeals()
                    val burgerDeals = viewModel.getBurgerDeals()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (studentDeals.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Student Deals",
                                    subtitle = "Value packed deals for students & friends"
                                )
                            }
                            items(studentDeals) { item ->
                                HorizontalFoodCard(
                                    foodItem = item,
                                    isFavorite = viewModel.isFavorite(item.id),
                                    onItemClick = { onNavigateToFoodDetail(item) },
                                    onAddToCartClick = { viewModel.addToCart(item) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                                )
                            }
                        }

                        if (pizzaDeals.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                                SectionHeader(
                                    title = "Pizza Deals",
                                    subtitle = "Mega pizza combo feasts with cold drinks & sides"
                                )
                            }
                            items(pizzaDeals) { item ->
                                HorizontalFoodCard(
                                    foodItem = item,
                                    isFavorite = viewModel.isFavorite(item.id),
                                    onItemClick = { onNavigateToFoodDetail(item) },
                                    onAddToCartClick = { viewModel.addToCart(item) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                                )
                            }
                        }

                        if (burgerDeals.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                                SectionHeader(
                                    title = "Burger Deals",
                                    subtitle = "Zinger & patty combos with sides & cold drinks"
                                )
                            }
                            items(burgerDeals) { item ->
                                HorizontalFoodCard(
                                    foodItem = item,
                                    isFavorite = viewModel.isFavorite(item.id),
                                    onItemClick = { onNavigateToFoodDetail(item) },
                                    onAddToCartClick = { viewModel.addToCart(item) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(90.dp)) }
                    }
                }

                "burgers" -> {
                    // BURGERS ONLY
                    val burgerItems = viewModel.getItemsForCategory("burgers")

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            SectionHeader(
                                title = "Burgers",
                                subtitle = "${burgerItems.size} freshly grilled & crispy burgers"
                            )
                        }

                        items(burgerItems) { item ->
                            HorizontalFoodCard(
                                foodItem = item,
                                isFavorite = viewModel.isFavorite(item.id),
                                onItemClick = { onNavigateToFoodDetail(item) },
                                onAddToCartClick = { viewModel.addToCart(item) },
                                onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .clickable {
                                        viewModel.selectCategory("deals")
                                        onCategorySelected("deals")
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "🍔", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Looking for Burger Deals?",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Explore combo savings in the Deals section",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Go to Deals",
                                        tint = CheeseGoldPrimary
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(90.dp)) }
                    }
                }

                else -> {
                    // ANY OTHER CATEGORY: ONLY THAT CATEGORY'S ITEMS
                    val categoryItems = viewModel.getItemsForCategory(uiState.selectedCategoryId)

                    if (categoryItems.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.RestaurantMenu,
                            title = "No items available in this category.",
                            description = "Please check back later or explore other sections.",
                            actionLabel = "View All Categories",
                            onActionClick = {
                                viewModel.selectCategory("all")
                                onCategorySelected("all")
                            }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                SectionHeader(
                                    title = selectedCategory?.name ?: "Products",
                                    subtitle = "${categoryItems.size} items available"
                                )
                            }

                            items(categoryItems) { item ->
                                HorizontalFoodCard(
                                    foodItem = item,
                                    isFavorite = viewModel.isFavorite(item.id),
                                    onItemClick = { onNavigateToFoodDetail(item) },
                                    onAddToCartClick = { viewModel.addToCart(item) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                                )
                            }

                            item { Spacer(modifier = Modifier.height(90.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuCategoryChip(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSpecialDeals: Boolean = false
) {
    val containerColor = when {
        isSelected -> CheeseGoldPrimary
        isSpecialDeals -> CheeseRed.copy(alpha = 0.15f)
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isSelected -> Color.White
        isSpecialDeals -> CheeseRed
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSpecialDeals && !isSelected) 1.dp else 0.dp,
                color = if (isSpecialDeals) CheeseRed.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .testTag("menu_category_chip_${label.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon.isNotEmpty()) {
                Text(text = icon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = contentColor
            )
        }
    }
}
