package com.rroot.pediatricdose.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rroot.pediatricdose.R
import kotlinx.coroutines.delay

/**
 * Branded splash shown for ~2.5 s when the app launches.
 *
 * Features:
 *  - Navy → cyan radial gradient background.
 *  - Adaptive launcher logo centred.
 *  - App title in large white type.
 *  - Credit block: Salah Ahmod / Internal Medicine Resident /
 *    Instagram handle.
 *  - Fades all elements in over 600 ms.
 */
@Composable
fun PediCalcSplash(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        label = "splash-fade",
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        onFinished()
    }

    val gradient = Brush.radialGradient(
        colors = listOf(Color(0xFF0D47A1), Color(0xFF0277BD), Color(0xFF01579B)),
        radius = 1400f,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .alpha(alpha),
        ) {
            // Logo: launcher icon in a soft white circle
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(20.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.mipmap.ic_launcher_round),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                )
            }

            Spacer(Modifier.height(28.dp))
            Text(
                text = "PediCalc AI",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Pediatric dose calculator",
                color = Color(0xFFE0F7FA),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
            )

            Spacer(Modifier.height(48.dp))
            // Credit card
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.10f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Designed by",
                        color = Color(0xFFB3E5FC),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Salah Ahmod",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Internal Medicine Resident",
                        color = Color(0xFFE1F5FE),
                        fontSize = 13.sp,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Instagram   @salah_ahmod",
                        color = Color(0xFFFFD54F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(Modifier.height(36.dp))
            Text(
                text = "© 2026 Salah Ahmod  ·  All rights reserved",
                color = Color(0xFF90CAF9),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}
