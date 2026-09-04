package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CharcoalTextPrimary,
    onPrimary = CharcoalBackground,
    primaryContainer = CharcoalSurfaceVariant,
    onPrimaryContainer = CharcoalTextPrimary,
    secondary = DarkClay,
    onSecondary = CharcoalBackground,
    secondaryContainer = Color(0xFF4E3321),
    onSecondaryContainer = Color(0xFFFBE4D2),
    tertiary = DarkSage,
    onTertiary = CharcoalBackground,
    background = CharcoalBackground,
    onBackground = CharcoalTextPrimary,
    surface = CharcoalSurface,
    onSurface = CharcoalTextPrimary,
    surfaceVariant = CharcoalSurfaceVariant,
    onSurfaceVariant = CharcoalTextSecondary,
    outline = CharcoalBorder,
    outlineVariant = Color(0xFF332B27)
)

private val LightColorScheme = lightColorScheme(
    primary = WarmEspresso,
    onPrimary = Color.White,
    primaryContainer = WarmSandSurfaceVariant,
    onPrimaryContainer = WarmEspresso,
    secondary = WarmClay,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFBE8DB),
    onSecondaryContainer = Color(0xFF42210C),
    tertiary = SageGreen,
    onTertiary = Color.White,
    background = WarmSandLight,
    onBackground = TextPrimaryLight,
    surface = WarmSandSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = WarmSandSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = Color(0xFFEBE3DC)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our carefully crafted Warm Editorial theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
