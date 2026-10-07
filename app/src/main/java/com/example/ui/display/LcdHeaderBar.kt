package com.example.ui.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AngleUnit
import com.example.model.CalculatorMode
import com.example.ui.theme.KeyAlphaPink
import com.example.ui.theme.KeyShiftGold

@Composable
fun LcdHeaderBar(
    isShiftActive: Boolean,
    isAlphaActive: Boolean,
    isMemoryActive: Boolean,
    angleUnit: AngleUnit,
    currentMode: CalculatorMode,
    isNaturalDisplay: Boolean,
    hasHistoryUp: Boolean,
    hasHistoryDown: Boolean,
    screenTextColor: Color,
    modifier: Modifier = Modifier
) {
    val inactiveAlpha = 0.15f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shift & Alpha indicators
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "SHIFT",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isShiftActive) KeyShiftGold else screenTextColor.copy(alpha = inactiveAlpha)
            )
            Text(
                text = "ALPHA",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isAlphaActive) KeyAlphaPink else screenTextColor.copy(alpha = inactiveAlpha)
            )
            Text(
                text = "M",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isMemoryActive) screenTextColor else screenTextColor.copy(alpha = inactiveAlpha)
            )
        }

        // Mode & Angle Unit & Display
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Angle unit [D] / [R] / [G]
            Text(
                text = angleUnit.indicator,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = screenTextColor
            )

            // Natural Math Display indicator
            Text(
                text = "MATH",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (isNaturalDisplay) screenTextColor else screenTextColor.copy(alpha = inactiveAlpha)
            )

            // Current Mode tag if not standard
            if (currentMode != CalculatorMode.CALCULATE) {
                Text(
                    text = currentMode.shortIndicator,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = screenTextColor
                )
            }

            // Up / Down replay arrows
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = "▲",
                    fontSize = 7.sp,
                    color = if (hasHistoryUp) screenTextColor else screenTextColor.copy(alpha = inactiveAlpha)
                )
                Text(
                    text = "▼",
                    fontSize = 7.sp,
                    color = if (hasHistoryDown) screenTextColor else screenTextColor.copy(alpha = inactiveAlpha)
                )
            }
        }
    }
}
