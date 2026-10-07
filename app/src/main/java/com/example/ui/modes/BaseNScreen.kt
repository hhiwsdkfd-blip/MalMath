package com.example.ui.modes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BaseMode
import com.example.model.BaseNConverter
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun BaseNScreen(
    isArabic: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeBase by remember { mutableStateOf(BaseMode.DEC) }
    var inputStr by remember { mutableStateOf("255") }

    val parsedVal = remember(inputStr, activeBase) {
        try {
            BaseNConverter.parse(inputStr, activeBase)
        } catch (_: Exception) {
            0L
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("base_n_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الأنظمة العددية والعمليات المنطقية" else "Base-N & Logic Operations",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                    modifier = Modifier.testTag("base_back_btn")
                ) {
                    Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                }
            }
        }

        // Active Base Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BaseMode.values().forEach { mode ->
                    FilterChip(
                        selected = activeBase == mode,
                        onClick = { activeBase = mode },
                        label = { Text(mode.label, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f).testTag("chip_base_${mode.name}")
                    )
                }
            }
        }

        // Input field
        item {
            OutlinedTextField(
                value = inputStr,
                onValueChange = { inputStr = it },
                label = { Text("${activeBase.label} Input") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("base_input_field"),
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (activeBase == BaseMode.HEX) KeyboardType.Ascii else KeyboardType.Number
                )
            )
        }

        // Live simultaneous conversion display across DEC, HEX, BIN, OCT
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                modifier = Modifier.fillMaxWidth().testTag("base_conversions_card")
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BaseMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mode.label,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (mode == activeBase) KeyShiftGold else PrimaryCyan
                            )
                            Text(
                                text = BaseNConverter.format(parsedVal, mode),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                        }
                        if (mode != BaseMode.OCT) {
                            HorizontalDivider(color = Color(0xFF263238))
                        }
                    }
                }
            }
        }

        // Bitwise logic operations buttons
        item {
            Text(
                text = if (isArabic) "العمليات المنطقية (Bitwise Logic):" else "Bitwise Logic Operations:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        val res = BaseNConverter.bitNot(parsedVal)
                        inputStr = BaseNConverter.format(res, activeBase)
                    },
                    modifier = Modifier.weight(1f).testTag("btn_not")
                ) {
                    Text("NOT")
                }
                Button(
                    onClick = {
                        val res = BaseNConverter.shiftLeft(parsedVal, 1)
                        inputStr = BaseNConverter.format(res, activeBase)
                    },
                    modifier = Modifier.weight(1f).testTag("btn_shl")
                ) {
                    Text("<< 1")
                }
                Button(
                    onClick = {
                        val res = BaseNConverter.shiftRight(parsedVal, 1)
                        inputStr = BaseNConverter.format(res, activeBase)
                    },
                    modifier = Modifier.weight(1f).testTag("btn_shr")
                ) {
                    Text(">> 1")
                }
            }
        }
    }
}
