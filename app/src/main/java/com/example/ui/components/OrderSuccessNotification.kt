package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SuccessBlueBorder
import com.example.ui.theme.SuccessBlueDark
import com.example.ui.theme.SuccessBlueLight
import com.example.ui.theme.SuccessBluePrimary
import kotlinx.coroutines.delay

/**
 * Professional blue order-success notification component.
 * Displays a polished confirmation card with smooth entrance/exit animations,
 * high-contrast typography, and optional order reference number.
 */
@Composable
fun OrderSuccessNotification(
    visible: Boolean,
    orderId: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    autoDismissDurationMillis: Long = 5000L
) {
    // Automatically dismiss after duration while visible
    LaunchedEffect(visible, orderId) {
        if (visible) {
            delay(autoDismissDurationMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight + 100 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight + 100 },
            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 240)
        ),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.Transparent,
            shadowElevation = 10.dp,
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(
                    width = 1.2.dp,
                    color = SuccessBlueBorder.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable { onDismiss() }
                .testTag("order_success_notification")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                SuccessBlueDark,
                                SuccessBluePrimary,
                                SuccessBlueLight
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Checkmark Badge Icon
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f))
                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Order Confirmed",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Success Details
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🎉 Congratulations!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp
                                    ),
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.padding(top = 2.dp))

                            Text(
                                text = "Your order has been placed successfully.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                    lineHeight = 17.sp
                                ),
                                color = Color.White.copy(alpha = 0.95f)
                            )

                            // Show existing order ID if available
                            if (!orderId.isNullOrBlank()) {
                                Spacer(modifier = Modifier.padding(top = 3.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White.copy(alpha = 0.20f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Order #$orderId",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            letterSpacing = 0.5.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Optional Close Icon Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss notification",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
