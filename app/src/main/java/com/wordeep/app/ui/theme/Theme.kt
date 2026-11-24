package com.wordeep.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WordDeepColorScheme = lightColorScheme(
    primary = SkyBluePrimary,
    secondary = MeadowGreen,
    tertiary = WarmAmber,
    background = SoftCream,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkTextSlate,
    onSurface = DarkTextSlate
)

@Composable
fun WorDeepTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WordDeepColorScheme,
        typography = Typography,
        content = content
    )
}
