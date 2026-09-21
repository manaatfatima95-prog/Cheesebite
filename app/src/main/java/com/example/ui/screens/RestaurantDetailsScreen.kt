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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.ui.components.branding.CheeseBiteSymbol
import com.example.ui.theme.BrandCharcoalDark
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.FoodItem
import com.example.ui.components.HorizontalFoodCard
import com.example.ui.theme.CheeseGoldDark
import com.example.ui.theme.CheeseGoldPrimary
import com.example.ui.theme.CheeseRed
import com.example.ui.theme.HerbGreen
import com.example.viewmodel.CheeseBiteViewModel

@Composable
fun RestaurantDetailsScreen(
    viewModel: CheeseBiteViewModel,
    onBackClick: () -> Unit,
    onNavigateToFoodDetail: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    val activeCategory = uiState.categories.getOrNull(selectedCategoryIndex) ?: uiState.categories.first()
    val itemsForCategory = viewModel.getItemsForCategory(activeCategory.id)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("restaurant_details_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Header Image & Back Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_promo_deal),
                    contentDescription = "Cheese Bite Restaurant",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Header Badges
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BrandCharcoalDark)
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CheeseBiteSymbol(
                            size = 44.dp,
                            badgeShape = false
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Chesee Bite Resturent",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 21.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "Pizza • Burgers • Shawarma • Wraps",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Restaurant Info Cards
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Rating & Status Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = CheeseGoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "4.9 (120+ reviews)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(HerbGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "OPEN NOW",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = HerbGreen
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Timings
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Opening Time: 12:00 PM • Closing Time: 01:00 AM",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MANDI THROO, Near Hamza Traders Zafarwal Road",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Free Delivery
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DeliveryDining,
                            contentDescription = null,
                            tint = HerbGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Free Home Delivery available across Mandi Throo & surroundings",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = HerbGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Phone Dial Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CheeseGoldPrimary.copy(alpha = 0.15f))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:03209163877")
                                    }
                                    context.startActivity(intent)
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = CheeseGoldDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "0320-9163877",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CheeseGoldDark
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CheeseGoldPrimary.copy(alpha = 0.15f))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:03067526655")
                                    }
                                    context.startActivity(intent)
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = CheeseGoldDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "0306-7526655",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CheeseGoldDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Tab Row
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                        color = CheeseGoldPrimary
                    )
                }
            ) {
                uiState.categories.forEachIndexed { index, cat ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = "${cat.iconEmoji} ${cat.name}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (selectedCategoryIndex == index) CheeseGoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Items for the selected category
        items(itemsForCategory) { item ->
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
