package com.caolehuyhoang.tuan6.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    background = Background,
    surface = Color.White,
    onBackground = TextMain,
    onSurface = TextMain,
    error = Red
)

@Composable
fun BTTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, typography = Typography, content = content)
}
