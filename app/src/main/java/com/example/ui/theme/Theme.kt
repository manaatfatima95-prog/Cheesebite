package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CheeseGoldLight,
    onPrimary = Color.Black,
    primaryContainer = CheeseGoldContainerDark,
    onPrimaryContainer = CheeseGoldLight,
    secondary = CheeseRed,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A1212),
    onSecondaryContainer = Color(0xFFFFDAD6),
    tertiary = HerbGreen,
    onTertiary = Color.White,
    background = DarkCanvas,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = CheeseGoldPrimary,
    onPrimary = Color.White,
    primaryContainer = CheeseGoldContainerLight,
    onPrimaryContainer = CheeseGoldDark,
    secondary = CheeseRed,
    onSecondary = Color.White,
    secondaryContainer = CheeseRedLight,
    onSecondaryContainer = CheeseRedDark,
    tertiary = HerbGreen,
    onTertiary = Color.White,
    background = LightCanvas,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder
)

@Composable
fun CheeseBiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Alias for compatibility with template tests
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    CheeseBiteTheme(darkTheme = darkTheme, content = content)
}
