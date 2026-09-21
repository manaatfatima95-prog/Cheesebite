package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HerbGreen
import kotlinx.coroutines.delay

/**
 * Modern, professional bottom success notification displayed after a review
 * is confirmed as successfully added.
 *
 * Adheres strictly to the Cheese Bites design language, featuring:
 * - Smooth entrance and exit slide & fade animations
 * - Professional checkmark badge
 * - Clear, high-contrast typography in both Dark and Light themes
 * - Automatic dismissal with duplicate protection
 * - Accessible touch targets and non-blocking layout placement
 */
@Composable
fun ReviewSuccessNotification(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    eventId: String? = null,
    autoDismissDurationMillis: Long = 4500L
) {
    // Automatically dismiss after duration while visible.
    // Keying by (visible, eventId) ensures each unique submission produces
    // exactly one full-duration notification without duplicate restarts.
    LaunchedEffect(visible, eventId) {
        if (visible) {
            delay(autoDismissDurationMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight + 100 },
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        ) + fadeIn(
            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight + 100 },
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 220)
        ),
        modifier = modifier
    ) {
        val isDark = isSystemInDarkTheme()

        val cardBackground = if (isDark) {
            Color(0xFF181C20)
        } else {
            Color(0xFFFFFFFF)
        }

        val cardBorderColor = if (isDark) {
            Color(0xFF2E7D32).copy(alpha = 0.65f)
        } else {
            Color(0xFF4CAF50).copy(alpha = 0.5f)
        }

        val titleColor = if (isDark) {
            Color(0xFFF1F5F9)
        } else {
            Color(0xFF1E293B)
        }

        val subtitleColor = if (isDark) {
            Color(0xFF94A3B8)
        } else {
            Color(0xFF475569)
        }

        val closeIconColor = if (isDark) {
            Color(0xFF94A3B8)
        } else {
            Color(0xFF64748B)
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = cardBackground,
            shadowElevation = 8.dp,
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.2.dp,
                    color = cardBorderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onDismiss() }
                .testTag("review_success_notification")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Checkmark Success Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(HerbGreen)
                            .testTag("review_success_badge"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Confirmation message
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Your review has been added.",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                letterSpacing = 0.15.sp
                            ),
                            color = titleColor
                        )

                        Text(
                            text = "Thank you for submitting your review!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                lineHeight = 17.sp
                            ),
                            color = subtitleColor
                        )
                    }
                }

                // Close / Dismiss affordance
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("review_success_dismiss_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss notification",
                        tint = closeIconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
