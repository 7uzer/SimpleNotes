package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Warm Editorial Palette
val WarmEspresso = Color(0xFF2C2523)
val WarmSandLight = Color(0xFFF8F5F1)
val WarmSandSurface = Color(0xFFFFFFFF)
val WarmSandSurfaceVariant = Color(0xFFEDE7E1)
val WarmClay = Color(0xFF8B5E3C)
val SageGreen = Color(0xFF436553)
val BorderLight = Color(0xFFD7CDC5)
val TextPrimaryLight = Color(0xFF201B18)
val TextSecondaryLight = Color(0xFF6B6058)

// Dark Theme Colors
val CharcoalBackground = Color(0xFF151312)
val CharcoalSurface = Color(0xFF1E1A18)
val CharcoalSurfaceVariant = Color(0xFF2A2421)
val CharcoalTextPrimary = Color(0xFFEAE4DF)
val CharcoalTextSecondary = Color(0xFFA69B92)
val CharcoalBorder = Color(0xFF3D3530)
val DarkClay = Color(0xFFDEAA88)
val DarkSage = Color(0xFFA5CFB6)

// Note Tints for custom colorful cards
data class NoteColorTheme(
    val index: Int,
    val name: String,
    val lightBackground: Color,
    val darkBackground: Color,
    val lightBorder: Color,
    val darkBorder: Color,
    val dotColor: Color
)

val NOTE_COLOR_PALETTE = listOf(
    NoteColorTheme(
        index = 0,
        name = "Classic",
        lightBackground = Color(0xFFFFFFFF),
        darkBackground = Color(0xFF24201E),
        lightBorder = Color(0xFFE4DCD4),
        darkBorder = Color(0xFF3D3632),
        dotColor = Color(0xFF9E928A)
    ),
    NoteColorTheme(
        index = 1,
        name = "Amber",
        lightBackground = Color(0xFFFFF4E8),
        darkBackground = Color(0xFF362518),
        lightBorder = Color(0xFFF1D9C3),
        darkBorder = Color(0xFF593C27),
        dotColor = Color(0xFFE08D46)
    ),
    NoteColorTheme(
        index = 2,
        name = "Sage",
        lightBackground = Color(0xFFEDF5EE),
        darkBackground = Color(0xFF1B2B20),
        lightBorder = Color(0xFFD0E5D5),
        darkBorder = Color(0xFF2E4B37),
        dotColor = Color(0xFF569A6E)
    ),
    NoteColorTheme(
        index = 3,
        name = "Lavender",
        lightBackground = Color(0xFFF3EFFB),
        darkBackground = Color(0xFF262037),
        lightBorder = Color(0xFFDED6F3),
        darkBorder = Color(0xFF40365D),
        dotColor = Color(0xFF8F76CD)
    ),
    NoteColorTheme(
        index = 4,
        name = "Sky",
        lightBackground = Color(0xFFEAF3FB),
        darkBackground = Color(0xFF182635),
        lightBorder = Color(0xFFCCE1F4),
        darkBorder = Color(0xFF29425A),
        dotColor = Color(0xFF4F9CD8)
    ),
    NoteColorTheme(
        index = 5,
        name = "Rose",
        lightBackground = Color(0xFFFDECEE),
        darkBackground = Color(0xFF381F24),
        lightBorder = Color(0xFFF4CCD2),
        darkBorder = Color(0xFF58313A),
        dotColor = Color(0xFFDB5A73)
    )
)

fun getNoteColor(index: Int, isDark: Boolean): Color {
    val theme = NOTE_COLOR_PALETTE.getOrNull(index) ?: NOTE_COLOR_PALETTE[0]
    return if (isDark) theme.darkBackground else theme.lightBackground
}

fun getNoteBorderColor(index: Int, isDark: Boolean): Color {
    val theme = NOTE_COLOR_PALETTE.getOrNull(index) ?: NOTE_COLOR_PALETTE[0]
    return if (isDark) theme.darkBorder else theme.lightBorder
}
