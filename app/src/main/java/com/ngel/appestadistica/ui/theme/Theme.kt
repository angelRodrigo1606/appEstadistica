package com.ngel.appestadistica.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Blue80
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun AppEstadisticaTheme(content: @Composable () -> Unit) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
