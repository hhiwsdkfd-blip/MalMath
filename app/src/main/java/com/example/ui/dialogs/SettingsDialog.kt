package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AngleUnit
import com.example.ui.display.DisplayTheme
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun SettingsDialog(
    angleUnit: AngleUnit,
    onAngleUnitChange: (AngleUnit) -> Unit,
    isNaturalDisplay: Boolean,
    onNaturalDisplayToggle: (Boolean) -> Unit,
    displayTheme: DisplayTheme,
    onThemeChange: (DisplayTheme) -> Unit,
    isArabic: Boolean,
    onLanguageToggle: (Boolean) -> Unit,
    hapticEnabled: Boolean,
    onHapticToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(12.dp)
                .testTag("settings_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E242C),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF37474F))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "إعدادات الحاسبة (SETUP)" else "CALCULATOR SETUP",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = KeyShiftGold,
                            fontFamily = FontFamily.Monospace
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("settings_close_btn")) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                // 1. Language Toggle
                item {
                    Text(
                        text = if (isArabic) "لغة الواجهة (Language):" else "Language:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = isArabic,
                            onClick = { onLanguageToggle(true) },
                            label = { Text("العربية (Arabic)") },
                            modifier = Modifier.weight(1f).testTag("lang_ar_chip")
                        )
                        FilterChip(
                            selected = !isArabic,
                            onClick = { onLanguageToggle(false) },
                            label = { Text("English (EN)") },
                            modifier = Modifier.weight(1f).testTag("lang_en_chip")
                        )
                    }
                }

                // 2. Angle Units (DEG, RAD, GRAD)
                item {
                    Text(
                        text = if (isArabic) "وحدة قياس الزوايا (Angle Unit):" else "Angle Unit:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AngleUnit.values().forEach { unit ->
                            FilterChip(
                                selected = angleUnit == unit,
                                onClick = { onAngleUnitChange(unit) },
                                label = { Text(unit.indicator + " - " + unit.name) },
                                modifier = Modifier.weight(1f).testTag("angle_chip_${unit.name}")
                            )
                        }
                    }
                }

                // 3. Display Format (Natural Textbook vs Linear)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "عرض طبيعي (Natural Textbook)" else "Natural Textbook Display",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isArabic) "عرض الكسور والجذور بطريقة كتب الرياضيات" else "Render fractions and roots naturally",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = isNaturalDisplay,
                            onCheckedChange = onNaturalDisplayToggle,
                            modifier = Modifier.testTag("switch_natural_display")
                        )
                    }
                }

                // 4. LCD Screen Theme
                item {
                    Text(
                        text = if (isArabic) "مظهر شاشة LCD:" else "LCD Screen Style:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DisplayTheme.values().forEach { theme ->
                            FilterChip(
                                selected = displayTheme == theme,
                                onClick = { onThemeChange(theme) },
                                label = { Text(theme.label, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).testTag("theme_chip_${theme.name}")
                            )
                        }
                    }
                }

                // 5. Vibration / Haptic Feedback
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "الاهتزاز عند الضغط (Haptic)" else "Keypress Vibration",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Switch(
                            checked = hapticEnabled,
                            onCheckedChange = onHapticToggle,
                            modifier = Modifier.testTag("switch_haptic")
                        )
                    }
                }

                // 6. About App Info
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF101419)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "CLASS-CALC Scientific (Kotlin Edition)",
                                fontWeight = FontWeight.Bold,
                                color = KeyShiftGold,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isArabic)
                                    "آلة حاسبة علمية متقدمة مبنية 100% بلغة كوتلن (Kotlin) و Jetpack Compose، تدعم العرض الطبيعي للمعادلات، المصفوفات، المتجهات، الأعداد المركبة، التفاضل والتكامل، والإحصاء ومشاركة رمز QR."
                                else
                                    "Advanced scientific calculator built 100% in pure Kotlin & Jetpack Compose, with Natural Textbook Display, Matrices, Vectors, Complex Numbers, Calculus, Statistics, and QR Code sharing.",
                                fontSize = 10.5.sp,
                                color = Color(0xFF90A4AE),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
