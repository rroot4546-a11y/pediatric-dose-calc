package com.rroot.pediatricdose.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** User-facing theme preference. */
enum class ThemePref { System, Light, Dark }

private val LightColors = lightColorScheme(
    primary = Color(0xFF0D47A1),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4FF),
    onPrimaryContainer = Color(0xFF001B3D),

    secondary = Color(0xFF00838F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2EBF2),
    onSecondaryContainer = Color(0xFF002F33),

    tertiary = Color(0xFFEF6C00),
    onTertiary = Color.White,

    background = Color(0xFFF5F7FA),
    onBackground = Color(0xFF101418),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101418),
    surfaceVariant = Color(0xFFE3E7EC),
    onSurfaceVariant = Color(0xFF42474E),

    error = Color(0xFFB3261E),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8FBFFF),
    onPrimary = Color(0xFF002F66),
    primaryContainer = Color(0xFF134B86),
    onPrimaryContainer = Color(0xFFD0E4FF),

    secondary = Color(0xFF66D9E8),
    onSecondary = Color(0xFF00363B),
    secondaryContainer = Color(0xFF005059),
    onSecondaryContainer = Color(0xFFB2EBF2),

    tertiary = Color(0xFFFFB780),
    onTertiary = Color(0xFF522300),

    background = Color(0xFF0F1417),
    onBackground = Color(0xFFE2E5E8),
    surface = Color(0xFF151C20),
    onSurface = Color(0xFFE2E5E8),
    surfaceVariant = Color(0xFF2A2F33),
    onSurfaceVariant = Color(0xFFC2C7CC),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

/** Resolve a [ThemePref] into the actual dark/light flag. */
@Composable
fun resolveDark(pref: ThemePref): Boolean = when (pref) {
    ThemePref.System -> isSystemInDarkTheme()
    ThemePref.Light -> false
    ThemePref.Dark -> true
}

@Composable
fun PediatricDoseTheme(
    themePref: ThemePref = ThemePref.System,
    content: @Composable () -> Unit,
) {
    val darkTheme = resolveDark(themePref)
    val colors = if (darkTheme) DarkColors else LightColors
    val palette = appPaletteFor(darkTheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalAppPalette provides palette) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography(),
            content = content,
        )
    }
}
