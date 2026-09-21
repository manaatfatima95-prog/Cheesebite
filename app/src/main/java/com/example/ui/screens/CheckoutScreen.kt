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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryAddress
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.CheeseGoldDark
import com.example.ui.theme.CheeseGoldPrimary
import com.example.ui.theme.HerbGreen
import com.example.viewmodel.CheeseBiteViewModel

data class PaymentOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@Composable
fun CheckoutScreen(
    viewModel: CheeseBiteViewModel,
    onBackClick: () -> Unit,
    onOrderSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedPaymentMethod by remember { mutableStateOf("Cash on Delivery (COD)") }
    var deliveryNote by remember { mutableStateOf("Call upon reaching, near Hamza Traders") }
    var showAddAddressDialog by remember { mutableStateOf(false) }

    val paymentOptions = listOf(
        PaymentOption("cod", "Cash on Delivery (COD)", "Pay with cash when your food arrives", Icons.Default.Payments),
        PaymentOption("jazzcash", "JazzCash", "Transfer to 0320-9163877 (Cheese Bites)", Icons.Default.PhoneAndroid),
        PaymentOption("easypaisa", "EasyPaisa", "Transfer to 0306-7526655 (Cheese Bites)", Icons.Default.PhoneAndroid)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("checkout_screen")
    ) {
        // App Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Checkout & Delivery",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Delivery Address Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = CheeseGoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Delivery Address",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            TextButton(onClick = { showAddAddressDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = CheeseGoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Add New",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CheeseGoldPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // List of Addresses
                        uiState.addresses.forEach { addr ->
                            val isSelected = addr.id == uiState.selectedAddress.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) CheeseGoldPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, CheeseGoldPrimary) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.selectAddress(addr) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .border(
                                                2.dp,
                                                if (isSelected) CheeseGoldPrimary else Color.Gray,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(9.dp)
                                                    .clip(CircleShape)
                                                    .background(CheeseGoldPrimary)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = addr.label,
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (addr.isDefault) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(CheeseGoldPrimary.copy(alpha = 0.2f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Default",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontSize = 9.sp,
                                                            color = CheeseGoldDark,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = addr.fullAddress,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        if (addr.landmark.isNotBlank()) {
                                            Text(
                                                text = "Landmark: ${addr.landmark}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Rider Note
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DeliveryDining,
                                contentDescription = null,
                                tint = CheeseGoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Delivery Note for Rider",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = deliveryNote,
                            onValueChange = { deliveryNote = it },
                            placeholder = { Text("e.g. Leave with security, call upon arriving...") },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CheeseGoldPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Payment Method Selection
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = CheeseGoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Payment Method",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        paymentOptions.forEach { opt ->
                            val isSelected = selectedPaymentMethod == opt.title
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) CheeseGoldPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, CheeseGoldPrimary) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedPaymentMethod = opt.title }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = opt.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) CheeseGoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = opt.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = opt.subtitle,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .border(
                                                2.dp,
                                                if (isSelected) CheeseGoldPrimary else Color.Gray,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(9.dp)
                                                    .clip(CircleShape)
                                                    .background(CheeseGoldPrimary)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Summary Review Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Amount Payable",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Items (${uiState.cartItemCount})", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "Rs. ${uiState.subtotal.toInt()}", fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Delivery Fee", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "FREE", color = HerbGreen, fontWeight = FontWeight.Bold)
                        }

                        if (uiState.discount > 0.0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Discount", color = HerbGreen)
                                Text(text = "- Rs. ${uiState.discount.toInt()}", color = HerbGreen, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Grand Total",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
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

        // Bottom Confirm Button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = "Place Order • Rs. ${uiState.finalTotal.toInt()}",
                    isLoading = uiState.isOrderProcessing,
                    onClick = {
                        viewModel.placeOrder(
                            paymentMethod = selectedPaymentMethod,
                            note = deliveryNote,
                            onOrderPlaced = { orderId ->
                                onOrderSuccess(orderId)
                            }
                        )
                    },
                    modifier = Modifier.testTag("confirm_place_order_button")
                )
            }
        }
    }

    // Add Address Dialog
    if (showAddAddressDialog) {
        var label by remember { mutableStateOf("Home") }
        var fullAddress by remember { mutableStateOf("") }
        var landmark by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf(uiState.userProfile.phone) }

        AlertDialog(
            onDismissRequest = { showAddAddressDialog = false },
            title = {
                Text(
                    text = "Add Delivery Address",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = label,
                        onValueChange = { label = it },
                        label = { Text("Label (e.g. Home, Office, Shop)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = fullAddress,
                        onValueChange = { fullAddress = it },
                        label = { Text("House / Street / Area") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = landmark,
                        onValueChange = { landmark = it },
                        label = { Text("Nearby Landmark (e.g. Near Hamza Traders)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact Phone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (fullAddress.isNotBlank()) {
                            viewModel.addNewAddress(label, fullAddress, landmark, phone)
                            showAddAddressDialog = false
                        }
                    }
                ) {
                    Text("Save Address", fontWeight = FontWeight.Bold, color = CheeseGoldPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAddressDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
