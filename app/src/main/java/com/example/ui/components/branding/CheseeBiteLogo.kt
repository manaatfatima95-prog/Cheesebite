package com.example.ui.components.branding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BrandCharcoal
import com.example.ui.theme.BrandCharcoalDark
import com.example.ui.theme.BrandCheeseGold
import com.example.ui.theme.BrandCheeseYellow
import com.example.ui.theme.BrandDeepRed
import com.example.ui.theme.BrandWarmCream
import com.example.ui.theme.BrandWarmCreamMuted

/**
 * Chesee Bite Resturent - Brand Identity Logo Variants
 *
 * Implements the complete multi-asset logo system:
 * 1. PRIMARY: Stacked Symbol + "CHESEE BITE" + "RESTURENT"
 * 2. HORIZONTAL: Side-by-side Symbol + compact Wordmark
 * 3. ICON_ONLY: Standalone distinctive Cheese Bite symbol
 * 4. MONOCHROME: Single-color black/white representation
 * 5. DARK_BACKGROUND: High contrast on dark charcoal canvas
 * 6. LIGHT_BACKGROUND: Refined on warm cream / light surface
 */
enum class LogoVariant(val label: String, val description: String) {
    PRIMARY("Primary", "Symbol + Chesee Bite Resturent stacked lockup"),
    HORIZONTAL("Horizontal", "Side-by-side compact brand lockup"),
    ICON_ONLY("Icon", "Distinctive cheese-bite symbol only"),
    MONOCHROME("Monochrome", "High-contrast single-tone vector"),
    DARK_BG("Dark Canvas", "Optimized for deep charcoal surfaces"),
    LIGHT_BG("Light Canvas", "Optimized for warm cream surfaces")
}

/**
 * Renders the custom geometric Cheese Bite symbol.
 * Features a compact rounded cheese block with a deliberate circular bite impression
 * taken from the upper-right corner, subtle melted contour, and clean tone-on-tone holes.
 */
@Composable
fun CheeseBiteSymbol(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    monochromeColor: Color? = null,
    badgeShape: Boolean = false,
    badgeBackground: Color = BrandCharcoal
) {
    val content = @Composable {
        Canvas(modifier = Modifier.size(size)) {
            drawCheeseBiteIcon(monochromeColor = monochromeColor)
        }
    }

    if (badgeShape) {
        Box(
            modifier = modifier
                .size(size * 1.35f)
                .clip(RoundedCornerShape(size * 0.35f))
                .background(badgeBackground)
                .padding(size * 0.17f),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    } else {
        Box(
            modifier = modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

private fun DrawScope.drawCheeseBiteIcon(monochromeColor: Color?) {
    val w = size.width
    val h = size.height

    if (monochromeColor != null) {
        // Single color silhouette version
        val basePath = Path().apply {
            val rect = RoundRect(
                rect = Rect(w * 0.12f, h * 0.18f, w * 0.88f, h * 0.84f),
                cornerRadius = CornerRadius(w * 0.16f, h * 0.16f)
            )
            addRoundRect(rect)
        }
        val biteCutout = Path().apply {
            addOval(Rect(w * 0.62f, h * 0.10f, w * 0.98f, h * 0.46f))
        }
        val finalPath = Path().apply {
            op(basePath, biteCutout, PathOperation.Difference)
        }
        drawPath(finalPath, color = monochromeColor)

        // Subtle cheese holes in negative space (draw transparent circles or cutout)
        drawCircle(
            color = Color.Transparent,
            radius = w * 0.065f,
            center = Offset(w * 0.38f, h * 0.52f)
        )
        return
    }

    // Full color premium vector:
    // Base cheese block with rounded corners
    val cheeseBasePath = Path().apply {
        val rect = RoundRect(
            rect = Rect(w * 0.12f, h * 0.20f, w * 0.88f, h * 0.84f),
            cornerRadius = CornerRadius(w * 0.16f, h * 0.16f)
        )
        addRoundRect(rect)
    }

    // Primary bite mark: Clean circular indentation subtracted from top-right corner
    val biteCircle = Path().apply {
        addOval(Rect(w * 0.62f, h * 0.10f, w * 0.98f, h * 0.46f))
    }

    // Secondary subtle melted indentation
    val biteMelt = Path().apply {
        addOval(Rect(w * 0.76f, h * 0.40f, w * 0.92f, h * 0.56f))
    }

    val bittenCheese = Path().apply {
        op(cheeseBasePath, biteCircle, PathOperation.Difference)
    }
    val finalCheesePath = Path().apply {
        op(bittenCheese, biteMelt, PathOperation.Difference)
    }

    // Draw base shadow
    drawOval(
        color = Color(0x2B000000),
        topLeft = Offset(w * 0.16f, h * 0.84f),
        size = Size(w * 0.68f, h * 0.12f)
    )

    // Fill cheese block with vibrant golden cheese gradient
    val cheeseGradient = Brush.linearGradient(
        colors = listOf(
            BrandCheeseYellow,
            BrandCheeseGold,
            Color(0xFFE59500)
        ),
        start = Offset(w * 0.15f, h * 0.20f),
        end = Offset(w * 0.85f, h * 0.85f)
    )
    drawPath(finalCheesePath, brush = cheeseGradient)

    // Dimensional top crust highlight
    val topFacet = Path().apply {
        moveTo(w * 0.16f, h * 0.32f)
        lineTo(w * 0.32f, h * 0.20f)
        lineTo(w * 0.66f, h * 0.20f)
        lineTo(w * 0.62f, h * 0.36f)
        lineTo(w * 0.16f, h * 0.32f)
        close()
    }
    drawPath(topFacet, color = Color(0x33FFFFFF))

    // Tone-on-tone cheese holes
    val holeColor = Color(0xFFD47C00)
    drawCircle(
        color = holeColor,
        radius = w * 0.075f,
        center = Offset(w * 0.36f, h * 0.52f)
    )
    drawCircle(
        color = Color(0x40FFFFFF),
        radius = w * 0.025f,
        center = Offset(w * 0.34f, h * 0.50f)
    )

    drawCircle(
        color = holeColor,
        radius = w * 0.06f,
        center = Offset(w * 0.56f, h * 0.68f)
    )

    drawCircle(
        color = holeColor,
        radius = w * 0.045f,
        center = Offset(w * 0.58f, h * 0.44f)
    )

    // Deep Red restrained quality accent seal
    drawCircle(
        color = BrandDeepRed,
        radius = w * 0.038f,
        center = Offset(w * 0.28f, h * 0.74f)
    )
}

/**
 * Modern custom wordmark strictly displaying the brand name:
 * "Chesee Bite Resturent"
 */
@Composable
fun CheseeBiteWordmark(
    modifier: Modifier = Modifier,
    isDarkBackground: Boolean = true,
    isHorizontal: Boolean = false,
    fontSizeMultiplier: Float = 1f
) {
    val primaryTextColor = if (isDarkBackground) BrandWarmCream else BrandCharcoal
    val secondaryTextColor = if (isDarkBackground) BrandCheeseYellow else BrandCheeseGold

    if (isHorizontal) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Chesee",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = (19 * fontSizeMultiplier).sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = primaryTextColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Bite",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = (19 * fontSizeMultiplier).sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = secondaryTextColor
                )
            }
            Text(
                text = "RESTURENT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (9 * fontSizeMultiplier).sp,
                    letterSpacing = 2.4.sp
                ),
                color = primaryTextColor.copy(alpha = 0.75f)
            )
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Chesee",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = (26 * fontSizeMultiplier).sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = primaryTextColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Bite",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = (26 * fontSizeMultiplier).sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = secondaryTextColor
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "RESTURENT",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (11 * fontSizeMultiplier).sp,
                    letterSpacing = 3.8.sp
                ),
                color = primaryTextColor.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * Universal Chesee Bite Resturent Logo Lockup Component.
 * Supports all variants required by the brand system:
 * - PRIMARY: Stacked symbol + full wordmark
 * - HORIZONTAL: Inline icon + wordmark
 * - ICON_ONLY: Isolated cheese-bite symbol
 * - MONOCHROME: Single-color black or white
 * - DARK_BG: Deep charcoal card container
 * - LIGHT_BG: Warm cream card container
 */
@Composable
fun CheseeBiteLogoLockup(
    variant: LogoVariant = LogoVariant.PRIMARY,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else modifier

    when (variant) {
        LogoVariant.PRIMARY -> {
            Column(
                modifier = clickableModifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CheeseBiteSymbol(
                    size = iconSize,
                    badgeShape = true,
                    badgeBackground = BrandCharcoalDark
                )
                Spacer(modifier = Modifier.height(10.dp))
                CheseeBiteWordmark(
                    isDarkBackground = MaterialTheme.colorScheme.background.run {
                        // Estimate brightness
                        val luminance = (red * 0.299f + green * 0.587f + blue * 0.114f)
                        luminance < 0.5f
                    },
                    isHorizontal = false
                )
            }
        }

        LogoVariant.HORIZONTAL -> {
            Row(
                modifier = clickableModifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheeseBiteSymbol(
                    size = iconSize,
                    badgeShape = true,
                    badgeBackground = BrandCharcoalDark
                )
                Spacer(modifier = Modifier.width(10.dp))
                CheseeBiteWordmark(
                    isDarkBackground = MaterialTheme.colorScheme.background.run {
                        val luminance = (red * 0.299f + green * 0.587f + blue * 0.114f)
                        luminance < 0.5f
                    },
                    isHorizontal = true
                )
            }
        }

        LogoVariant.ICON_ONLY -> {
            CheeseBiteSymbol(
                modifier = clickableModifier,
                size = iconSize,
                badgeShape = true,
                badgeBackground = BrandCharcoalDark
            )
        }

        LogoVariant.MONOCHROME -> {
            val monoColor = if (MaterialTheme.colorScheme.background.run {
                (red * 0.299f + green * 0.587f + blue * 0.114f) < 0.5f
            }) Color.White else BrandCharcoal

            Row(
                modifier = clickableModifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheeseBiteSymbol(
                    size = iconSize,
                    monochromeColor = monoColor
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "CHESEE BITE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = monoColor
                    )
                    Text(
                        text = "RESTURENT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.5.sp
                        ),
                        color = monoColor.copy(alpha = 0.8f)
                    )
                }
            }
        }

        LogoVariant.DARK_BG -> {
            Card(
                modifier = clickableModifier,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandCharcoalDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFF2E2E36), Color(0xFF1E1E24))))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CheeseBiteSymbol(
                        size = iconSize,
                        badgeShape = false
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CheseeBiteWordmark(
                        isDarkBackground = true,
                        isHorizontal = true
                    )
                }
            }
        }

        LogoVariant.LIGHT_BG -> {
            Card(
                modifier = clickableModifier,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandWarmCream),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFFE8E4D8), Color(0xFFF2ECE0))))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CheeseBiteSymbol(
                        size = iconSize,
                        badgeShape = true,
                        badgeBackground = BrandCharcoalDark
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CheseeBiteWordmark(
                        isDarkBackground = false,
                        isHorizontal = true
                    )
                }
            }
        }
    }
}

/**
 * Interactive Brand Identity & Logo System Viewer Dialog.
 * Allows the user or stakeholders to inspect all 6 deliverables in the logo system:
 * 1. Primary Logo
 * 2. Horizontal Logo
 * 3. Icon Logo
 * 4. Monochrome Logo
 * 5. Dark Background Version
 * 6. Light Background Version
 * Along with specifications for Web, App Icon, Print, and Packaging.
 */
@Composable
fun BrandIdentityShowcaseDialog(
    onDismiss: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val variants = LogoVariant.values()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("brand_identity_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BrandCheeseYellow.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = BrandCheeseYellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Brand Logo System",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Chesee Bite Resturent",
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandCheeseGold
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Tabs for the 6 deliverables
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = BrandCheeseYellow
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Primary", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Horizontal", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("Icon", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 3,
                        onClick = { selectedTabIndex = 3 },
                        text = { Text("Variants", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Preview Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            when (selectedTabIndex) {
                                0 -> BrandCharcoalDark
                                1 -> BrandCharcoal
                                2 -> BrandCharcoalDark
                                else -> BrandWarmCream
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedTabIndex) {
                        0 -> CheseeBiteLogoLockup(variant = LogoVariant.PRIMARY, iconSize = 56.dp)
                        1 -> CheseeBiteLogoLockup(variant = LogoVariant.HORIZONTAL, iconSize = 48.dp)
                        2 -> CheseeBiteLogoLockup(variant = LogoVariant.ICON_ONLY, iconSize = 72.dp)
                        3 -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CheseeBiteLogoLockup(variant = LogoVariant.MONOCHROME, iconSize = 36.dp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Monochrome / Packaging Ready",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandCharcoal.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand Identity Attributes
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Brand Name",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Chesee Bite Resturent",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Color Palette",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Charcoal • Golden Yellow • Cream",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Applications",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "App Icon • Web Favicon • Packaging • Signage",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
