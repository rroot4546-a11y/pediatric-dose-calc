package com.rroot.pediatricdose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Application-specific accent palette in addition to the standard
 * Material 3 [androidx.compose.material3.ColorScheme].
 *
 * The screens (Diagnoses / Syrups / Injections) use coloured pill rows
 * in cyan / yellow / red / grey to mirror the bedside dosing card the
 * app replaces. We keep those four accents tonally consistent across
 * light and dark mode by exposing them through this palette and a
 * companion CompositionLocal — that way every screen automatically
 * picks up the right contrast values when the user toggles the theme.
 */
data class AppPalette(
    /** Soft pill background under "fluid / antibiotic" rows. */
    val accentCyan: Color,
    /** Soft pill background under "bolus / antiepileptic" rows. */
    val accentYellow: Color,
    /** Soft pill background under "neutral / maintenance" rows. */
    val accentGray: Color,
    /** Soft pill background under "emergency" rows. */
    val accentRed: Color,
    /** Stroke around pills + section dividers. */
    val border: Color,
    /** Drug-name / label foreground (high contrast on accents). */
    val label: Color,
    /** Computed-dose value foreground (high contrast on accents). */
    val value: Color,
    /** Capped / warning text. */
    val warn: Color,
    /** Secondary explanatory copy below pills. */
    val secondaryText: Color,
    /** Reference / footnote copy. */
    val mutedText: Color,
    /** Background behind cards in detail screens. */
    val cardBg: Color,
)

private val LightPalette = AppPalette(
    accentCyan = Color(0xFFB2EBF2),
    accentYellow = Color(0xFFFFF59D),
    accentGray = Color(0xFFE0E0E0),
    accentRed = Color(0xFFFFCDD2),
    border = Color(0xFFB0BEC5),
    label = Color(0xFF0D47A1),
    value = Color(0xFFC62828),
    warn = Color(0xFFB71C1C),
    secondaryText = Color(0xFF37474F),
    mutedText = Color(0xFF607D8B),
    cardBg = Color(0xFFF5F7FA),
)

private val DarkPalette = AppPalette(
    // Dimmer pill backgrounds tuned for low-light reading. We keep the
    // hue but drop saturation/value so the bright label text stays
    // readable on top.
    accentCyan = Color(0xFF1B3B45),
    accentYellow = Color(0xFF3F3919),
    accentGray = Color(0xFF2A2F33),
    accentRed = Color(0xFF4A1F1F),
    border = Color(0xFF455A64),
    label = Color(0xFFB3E5FC),
    value = Color(0xFFFF8A80),
    warn = Color(0xFFFF8A80),
    secondaryText = Color(0xFFCFD8DC),
    mutedText = Color(0xFF90A4AE),
    cardBg = Color(0xFF151C20),
)

internal val LocalAppPalette = staticCompositionLocalOf { LightPalette }

/** Pick the right palette for [isDark]. */
fun appPaletteFor(isDark: Boolean): AppPalette = if (isDark) DarkPalette else LightPalette

/** Convenience accessor inside composables. */
val AppColors: AppPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalAppPalette.current
