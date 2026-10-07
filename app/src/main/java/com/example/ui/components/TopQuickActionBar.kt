package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AngleUnit
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

enum class DecimalDisplayFormat(val label: String) {
    DECI("DECI"),
    NORM("NORM"),
    FIX("FIX"),
    SCI("SCI")
}

@Composable
fun TopQuickActionBar(
    angleUnit: AngleUnit,
    onAngleUnitCycle: () -> Unit,
    isNaturalDisplay: Boolean,
    onToggleNaturalDisplay: () -> Unit,
    decimalFormat: DecimalDisplayFormat,
    onDecimalFormatCycle: () -> Unit,
    isMemoryActive: Boolean,
    onOpenMemoryDialog: () -> Unit,
    onOpenMenu: () -> Unit,
    onInsertSqrt: () -> Unit,
    onResetCurrent: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onOpenCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_quick_action_bar"),
        color = Color(0xFF141920),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 1. ☰ Menu Button
            QuickBarIconButton(
                tag = "quick_btn_menu",
                onClick = onOpenMenu
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(16.dp))
            }

            // 2. √x Quick Root Button
            QuickBarPillButton(
                label = "√x",
                tag = "quick_btn_sqrt",
                textColor = KeyShiftGold,
                onClick = onInsertSqrt
            )

            // 3. ↻ Reset/Refresh Button
            QuickBarIconButton(
                tag = "quick_btn_refresh",
                onClick = onResetCurrent
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color(0xFFEF5350), modifier = Modifier.size(16.dp))
            }

            // 4. Circle Minus (Zoom Out Display)
            QuickBarIconButton(
                tag = "quick_btn_zoom_out",
                onClick = onZoomOut
            ) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Zoom Out", tint = Color.LightGray, modifier = Modifier.size(16.dp))
            }

            // 5. Circle Plus (Zoom In Display)
            QuickBarIconButton(
                tag = "quick_btn_zoom_in",
                onClick = onZoomIn
            ) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Zoom In", tint = Color.LightGray, modifier = Modifier.size(16.dp))
            }

            // 6. Camera Photo Math Scanner
            QuickBarIconButton(
                tag = "quick_btn_camera",
                onClick = onOpenCamera
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Camera Math", tint = PrimaryCyan, modifier = Modifier.size(16.dp))
            }

            // 7. DEG / RAD / GRAD Quick Toggle
            QuickBarPillButton(
                label = angleUnit.indicator,
                subLabel = angleUnit.name,
                tag = "quick_btn_angle",
                textColor = PrimaryCyan,
                onClick = onAngleUnitCycle
            )

            // 8. M ▼ Memory Indicator & Manager
            QuickBarPillButton(
                label = "M ▼",
                tag = "quick_btn_memory",
                textColor = if (isMemoryActive) KeyShiftGold else Color.Gray,
                bgColor = if (isMemoryActive) Color(0xFF332915) else Color(0xFF1E2631),
                onClick = onOpenMemoryDialog
            )

            // 9. MATH Toggle
            QuickBarPillButton(
                label = "MATH",
                tag = "quick_btn_math",
                textColor = if (isNaturalDisplay) Color.White else Color.DarkGray,
                bgColor = if (isNaturalDisplay) Color(0xFF004D73) else Color(0xFF1E2631),
                onClick = onToggleNaturalDisplay
            )

            // 10. DECI / NORM / FIX / SCI Format Toggle
            QuickBarPillButton(
                label = decimalFormat.label,
                tag = "quick_btn_deci",
                textColor = Color(0xFF81C784),
                onClick = onDecimalFormatCycle
            )
        }
    }
}

@Composable
private fun QuickBarIconButton(
    tag: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1E2631))
            .border(1.dp, Color(0xFF2C3642), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun QuickBarPillButton(
    label: String,
    subLabel: String? = null,
    tag: String,
    textColor: Color,
    bgColor: Color = Color(0xFF1E2631),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, Color(0xFF2C3642), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = textColor
            )
            if (subLabel != null) {
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = subLabel,
                    fontSize = 8.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
