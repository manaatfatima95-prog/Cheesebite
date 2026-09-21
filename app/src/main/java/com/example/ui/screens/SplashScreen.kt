package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.branding.CheeseBiteSymbol
import com.example.ui.components.branding.CheseeBiteWordmark
import com.example.ui.theme.BrandCharcoalDark
import com.example.ui.theme.BrandCheeseYellow
import com.example.ui.theme.CheeseGoldPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(600))
        scale.animateTo(1f, animationSpec = tween(600))
        delay(1200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            Box(
                modifier = Modifier
                    .size(136.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(BrandCharcoalDark)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                CheeseBiteSymbol(
                    size = 96.dp,
                    badgeShape = false
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            CheseeBiteWordmark(
                isDarkBackground = MaterialTheme.colorScheme.background.run {
                    (red * 0.299f + green * 0.587f + blue * 0.114f) < 0.5f
                },
                isHorizontal = false,
                fontSizeMultiplier = 1.15f
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Delicious Food • Quality • Pure Chesee",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.2.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = BrandCheeseYellow,
                strokeWidth = 3.dp,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
