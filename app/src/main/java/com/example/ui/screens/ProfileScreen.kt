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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CheeseGoldDark
import com.example.ui.theme.CheeseGoldPrimary
import com.example.ui.theme.CheeseRed
import com.example.ui.theme.HerbGreen
import com.example.viewmodel.CheeseBiteViewModel

@Composable
fun ProfileScreen(
    viewModel: CheeseBiteViewModel,
    onNavigateToFavorites: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToRestaurant: () -> Unit,
    onNavigateToChatbot: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("profile_screen")
    ) {
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
                    text = "My Profile & Settings",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(CheeseGoldPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = CheeseGoldPrimary,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = uiState.userProfile.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = uiState.userProfile.phone,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = uiState.userProfile.email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(onClick = { showEditProfileDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = CheeseGoldPrimary
                            )
                        }
                    }
                }
            }

            // Quick Shortcuts Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ProfileMenuItem(
                            icon = Icons.Default.ReceiptLong,
                            title = "Order History",
                            subtitle = "View past orders & reorder quickly",
                            onClick = onNavigateToOrders
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        ProfileMenuItem(
                            icon = Icons.Default.Favorite,
                            title = "My Favorite Bites",
                            subtitle = "${uiState.favoriteItemIds.size} saved dishes",
                            onClick = onNavigateToFavorites
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        ProfileMenuItem(
                            icon = Icons.Default.SmartToy,
                            title = "Cheese Bites AI Assistant",
                            subtitle = "Ask about menu, prices, deals & hours",
                            onClick = onNavigateToChatbot
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        ProfileMenuItem(
                            icon = Icons.Default.Star,
                            title = "Rate & Review Cheese Bite",
                            subtitle = "Share your dining feedback",
                            onClick = { showReviewDialog = true }
                        )
                    }
                }
            }

            // App Preferences (Dark Mode Switch)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Preferences",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (uiState.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null,
                                    tint = CheeseGoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Dark Theme",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (uiState.isDarkTheme) "Deep charcoal nighttime look" else "Clean bright daytime look",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = uiState.isDarkTheme,
                                onCheckedChange = { viewModel.toggleTheme() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = CheeseGoldPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Restaurant Info Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = CheeseGoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "About Cheese Bites Restaurant",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            TextButton(onClick = onNavigateToRestaurant) {
                                Text(
                                    text = "Details",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CheeseGoldPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "MANDI THROO, Near Hamza Traders Zafarwal Road\n" +
                                    "Timings: 12:00 PM - 01:00 AM Daily\n" +
                                    "Hot & Cheesy Goodness with Free Home Delivery",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = null,
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
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = null,
                                        tint = CheeseGoldDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "0306-7526655",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = CheeseGoldDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var name by remember { mutableStateOf(uiState.userProfile.name) }
        var phone by remember { mutableStateOf(uiState.userProfile.phone) }
        var email by remember { mutableStateOf(uiState.userProfile.email) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateProfile(name, phone, email)
                        showEditProfileDialog = false
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, color = CheeseGoldPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Review & Rating Dialog
    if (showReviewDialog) {
        var rating by remember { mutableDoubleStateOf(5.0) }
        var reviewText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = {
                Text(
                    text = "Rate Cheese Bite",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "How was your food experience?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star Stars",
                                tint = if (star <= rating) CheeseGoldPrimary else Color.LightGray,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { rating = star.toDouble() }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        placeholder = { Text("Write your review (e.g. Delicious crown crust pizza!)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.submitReview(
                            rating = rating,
                            reviewText = reviewText,
                            onSuccess = {
                                showReviewDialog = false
                            }
                        )
                    },
                    enabled = !uiState.isSubmittingReview
                ) {
                    if (uiState.isSubmittingReview) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = CheeseGoldPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Submit Review", fontWeight = FontWeight.Bold, color = CheeseGoldPrimary)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showReviewDialog = false },
                    enabled = !uiState.isSubmittingReview
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CheeseGoldPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CheeseGoldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}
