package com.example.ui.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AngleUnit
import com.example.model.CalculatorMode
import com.example.model.Fraction
import com.example.ui.theme.*

enum class DisplayTheme(val label: String, val bg: Color, val text: Color, val subtle: Color) {
    CASIO_LCD("Casio LCD", LcdScreenBgCasio, LcdScreenContentCasio, LcdScreenSubtleCasio),
    DARK_OLED("Dark OLED", LcdScreenBgDark, LcdScreenContentDark, LcdScreenSubtleDark),
    PAPER_WHITE("Paper Light", Color(0xFFF7F9FB), Color(0xFF1E2631), Color(0xFF78909C))
}

@Composable
fun NaturalMathDisplay(
    expression: String,
    cursorPosition: Int,
    result: String,
    exactResult: String?,
    isExactMode: Boolean,
    errorMessage: String?,
    isShiftActive: Boolean,
    isAlphaActive: Boolean,
    isMemoryActive: Boolean,
    angleUnit: AngleUnit,
    currentMode: CalculatorMode,
    isNaturalDisplay: Boolean,
    hasHistoryUp: Boolean,
    hasHistoryDown: Boolean,
    displayTheme: DisplayTheme,
    fontScale: Float = 1.0f,
    onSdToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Flashing cursor animation
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorVisible by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_blink"
    )

    LaunchedEffect(expression.length) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(displayTheme.bg)
            .border(2.dp, Color(0xFF0A0D11), RoundedCornerShape(10.dp))
            .padding(2.dp)
            .testTag("natural_math_display")
    ) {
        // LCD Dot Matrix Background Texture
        Canvas(modifier = Modifier.matchParentSize()) {
            val dotSpacing = 8.dp.toPx()
            val dotColor = displayTheme.text.copy(alpha = 0.025f)
            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(
                        color = dotColor,
                        radius = 0.8f,
                        center = Offset(x, y)
                    )
                    y += dotSpacing
                }
                x += dotSpacing
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            // Header bar with Shift/Alpha/Mode indicators
            LcdHeaderBar(
                isShiftActive = isShiftActive,
                isAlphaActive = isAlphaActive,
                isMemoryActive = isMemoryActive,
                angleUnit = angleUnit,
                currentMode = currentMode,
                isNaturalDisplay = isNaturalDisplay,
                hasHistoryUp = hasHistoryUp,
                hasHistoryDown = hasHistoryDown,
                screenTextColor = displayTheme.text
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Main Input Expression Line (Textbook Display)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .horizontalScroll(scrollState),
                contentAlignment = Alignment.CenterStart
            ) {
                if (isNaturalDisplay) {
                    RenderNaturalExpression(
                        expr = expression,
                        cursorPos = cursorPosition,
                        cursorAlpha = if (cursorVisible > 0.5f) 1f else 0f,
                        textColor = displayTheme.text,
                        fontScale = fontScale
                    )
                } else {
                    // Standard Linear Display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val beforeCursor = expression.take(cursorPosition)
                        val afterCursor = expression.drop(cursorPosition)
                        Text(
                            text = beforeCursor.ifEmpty { if (afterCursor.isEmpty()) "" else "" },
                            fontSize = (20 * fontScale).sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            color = displayTheme.text
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(22.dp)
                                .background(displayTheme.text.copy(alpha = if (cursorVisible > 0.5f) 1f else 0f))
                        )
                        Text(
                            text = afterCursor,
                            fontSize = (20 * fontScale).sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            color = displayTheme.text
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Error or Result Display
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            } else if (result.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSdToggle() },
                    horizontalAlignment = Alignment.End
                ) {
                    // Exact vs Decimal toggle indicator badge [S<=>D]
                    if (exactResult != null && exactResult != result) {
                        Text(
                            text = if (isExactMode) "◄ EXACT [S⇔D] ►" else "◄ APPROX [S⇔D] ►",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = displayTheme.subtle,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Render result: stacked fraction if exact rational or formatted number
                    if (isExactMode && exactResult != null && exactResult.contains("/")) {
                        RenderStackedResultFraction(
                            fractionStr = exactResult,
                            textColor = displayTheme.text
                        )
                    } else {
                        Text(
                            text = result,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = displayTheme.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                // Empty state baseline spacer
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

/**
 * Natural Textbook Expression Renderer with support for stacked fractions,
 * square roots with top vinculum, powers in superscript, and cursor indicator.
 */
@Composable
private fun RenderNaturalExpression(
    expr: String,
    cursorPos: Int,
    cursorAlpha: Float,
    textColor: Color,
    fontScale: Float = 1.0f
) {
    if (expr.isEmpty()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(22.dp)
                    .background(textColor.copy(alpha = cursorAlpha))
            )
        }
        return
    }

    // Check if the expression contains natural fraction notation like (num)/(den) or a/b
    val formattedExpr = formatMathSymbols(expr)
    val beforeCursor = formattedExpr.take(cursorPos.coerceAtMost(formattedExpr.length))
    val afterCursor = formattedExpr.drop(cursorPos.coerceAtMost(formattedExpr.length))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = beforeCursor,
            fontSize = (20 * fontScale).sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace,
            color = textColor
        )
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(22.dp)
                .background(textColor.copy(alpha = cursorAlpha))
        )
        Text(
            text = afterCursor,
            fontSize = (20 * fontScale).sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace,
            color = textColor
        )
    }
}

@Composable
private fun RenderStackedResultFraction(fractionStr: String, textColor: Color) {
    val parts = fractionStr.split("/")
    if (parts.size == 2) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            Text(
                text = parts[0].trim(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = textColor
            )
            HorizontalDivider(
                modifier = Modifier.width(36.dp),
                thickness = 1.5.dp,
                color = textColor
            )
            Text(
                text = parts[1].trim(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = textColor
            )
        }
    } else {
        Text(
            text = fractionStr,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = textColor
        )
    }
}

private fun formatMathSymbols(s: String): String {
    return s.replace("*", "×")
        .replace("/", "÷")
        .replace("-", "−")
        .replace("sqrt(", "√(")
        .replace("cbrt(", "∛(")
        .replace("integrate(", "∫(")
        .replace("diff(", "d/dx(")
        .replace("sum(", "Σ(")
}
