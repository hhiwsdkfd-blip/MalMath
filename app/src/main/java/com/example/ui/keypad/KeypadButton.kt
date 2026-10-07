package com.example.ui.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class KeyConfig(
    val main: String,
    val shift: String? = null,
    val alpha: String? = null,
    val tag: String,
    val bgColor: Color = KeyNumberBg,
    val textColor: Color = KeyNumberText,
    val fontSize: TextUnit = 16.sp,
    val isPrimaryAction: Boolean = false
)

@Composable
fun KeypadButton(
    config: KeyConfig,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 44.dp
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .padding(2.dp)
            .testTag(config.tag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Upper Shift & Alpha hints
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = config.shift ?: "",
                color = KeyShiftGold,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
            Text(
                text = config.alpha ?: "",
                color = KeyAlphaPink,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        // Main 3D Beveled Key Surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(6.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = Color.White.copy(alpha = 0.3f))
                ) { onClick() },
            shape = RoundedCornerShape(6.dp),
            color = config.bgColor,
            shadowElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF37474F).copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.main,
                    color = config.textColor,
                    fontSize = config.fontSize,
                    fontWeight = if (config.isPrimaryAction) FontWeight.Black else FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
