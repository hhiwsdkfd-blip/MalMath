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
    primary = PrimaryCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF004D73),
    onPrimaryContainer = Color(0xFFCBE6FF),
    secondary = KeyShiftGold,
    onSecondary = Color.Black,
    secondaryContainer = KeyShiftGoldBg,
    tertiary = KeyAlphaPink,
    background = CalcChassisDark,
    surface = CalcChassisBezel,
    surfaceVariant = CalcChassisBorder,
    onBackground = Color(0xFFE2EBF5),
    onSurface = Color(0xFFE2EBF5)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryCyanVariant,
    onPrimary = Color.White,
    secondary = Color(0xFFF57F17),
    tertiary = Color(0xFFC2185B),
    background = Color(0xFFEBEFF2),
    surface = Color(0xFFF5F7FA),
    onBackground = Color(0xFF101418),
    onSurface = Color(0xFF101418)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
