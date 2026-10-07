package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SolarPanelBar(
    solarActive: Boolean,
    batteryPercent: Int,
    isArabic: Boolean,
    onToggleSolarLight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "solar_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("solar_panel_bar"),
        color = Color(0xFF101419),
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand & Dual Power Title
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "CLASS-CALC",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFFE0E0E0),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TWO WAY POWER",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (solarActive) KeyShiftGold else Color(0xFF78909C)
                    )
                }
                Text(
                    text = if (isArabic) "طاقة شمسية + بطارية" else "SOLAR & BATTERY",
                    fontSize = 8.sp,
                    color = Color(0xFF90A4AE)
                )
            }

            // Photovoltaic Solar Cell Grid (Interactive!)
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = if (solarActive) {
                                listOf(
                                    SolarCellDark,
                                    Color(0xFF281E48),
                                    Color(0xFF382A60),
                                    SolarCellDark
                                )
                            } else {
                                listOf(Color(0xFF1A1A1A), Color(0xFF222222), Color(0xFF1A1A1A))
                            }
                        )
                    )
                    .clickable { onToggleSolarLight() }
                    .testTag("solar_cell_button"),
                contentAlignment = Alignment.Center
            ) {
                // Photovoltaic cell stripes
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val step = size.width / 5f
                    for (i in 1..4) {
                        drawLine(
                            color = SolarCellGrid,
                            start = Offset(i * step, 0f),
                            end = Offset(i * step, size.height),
                            strokeWidth = 1.5f
                        )
                    }
                    drawLine(
                        color = SolarCellGrid,
                        start = Offset(0f, size.height / 2f),
                        end = Offset(size.width, size.height / 2f),
                        strokeWidth = 1f
                    )
                }

                // Solar active indicator glow
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Solar",
                        tint = if (solarActive) KeyShiftGold.copy(alpha = glowAlpha) else Color(0xFF546E7A),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (solarActive) "SOLAR ON" else "LOW LIGHT",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (solarActive) KeyShiftGold else Color(0xFF78909C),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Battery & Mode State
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (solarActive) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                    contentDescription = "Battery",
                    tint = if (batteryPercent > 20) Color(0xFF81C784) else Color(0xFFE57373),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "$batteryPercent%",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB0BEC5),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
