package com.example.ui.modes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
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
import com.example.model.OneVarStats
import com.example.model.StatisticsEngine
import com.example.model.TwoVarStats
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun StatisticsScreen(
    isArabic: Boolean,
    onShowQr: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isTwoVarMode by remember { mutableStateOf(false) }

    // 1-Var Data: list of (x, freq)
    var oneVarData by remember {
        mutableStateOf(
            mutableListOf(
                Pair(10.0, 1),
                Pair(12.0, 2),
                Pair(15.0, 3),
                Pair(18.0, 1),
                Pair(20.0, 2)
            )
        )
    }

    // 2-Var Data: list of (x, y)
    var twoVarData by remember {
        mutableStateOf(
            mutableListOf(
                Pair(1.0, 2.1),
                Pair(2.0, 3.9),
                Pair(3.0, 6.2),
                Pair(4.0, 7.8),
                Pair(5.0, 10.1)
            )
        )
    }

    // New entry inputs
    var inputX by remember { mutableStateOf("") }
    var inputYOrFreq by remember { mutableStateOf("1") }

    // Calculated results
    var oneStats by remember { mutableStateOf<OneVarStats?>(null) }
    var twoStats by remember { mutableStateOf<TwoVarStats?>(null) }

    LaunchedEffect(oneVarData, twoVarData, isTwoVarMode) {
        if (!isTwoVarMode) {
            oneStats = StatisticsEngine.calculateOneVar(oneVarData)
        } else {
            twoStats = StatisticsEngine.calculateLinearRegression(twoVarData)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("statistics_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الإحصاء والانحدار الرياضي" else "Statistics & Regression",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = {
                            val summary = if (!isTwoVarMode) {
                                "1-Var Stats:\nn=${oneStats?.n}, mean=${oneStats?.mean}, s=${oneStats?.sampleStdDev}"
                            } else {
                                "Linear Regression:\ny = ${twoStats?.a} + ${twoStats?.b}x\nr = ${twoStats?.r}"
                            }
                            onShowQr(summary)
                        },
                        modifier = Modifier.testTag("stat_qr_btn")
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = "QR Code", tint = KeyShiftGold)
                    }
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                        modifier = Modifier.testTag("stat_back_btn")
                    ) {
                        Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                    }
                }
            }
        }

        // Toggle 1-Var vs 2-Var Regression
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = if (isTwoVarMode) 1 else 0) {
                    Tab(
                        selected = !isTwoVarMode,
                        onClick = { isTwoVarMode = false },
                        text = { Text(if (isArabic) "متغير واحد (1-Variable)" else "1-Variable (x)") }
                    )
                    Tab(
                        selected = isTwoVarMode,
                        onClick = { isTwoVarMode = true },
                        text = { Text(if (isArabic) "انحدار خطي (Linear y=a+bx)" else "2-Var (y=a+bx)") }
                    )
                }
            }
        }

        // Add Data Point Row
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = inputX,
                        onValueChange = { inputX = it },
                        label = { Text("x") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("stat_input_x"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = inputYOrFreq,
                        onValueChange = { inputYOrFreq = it },
                        label = { Text(if (isTwoVarMode) "y" else "Freq") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("stat_input_y_or_freq"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    IconButton(
                        onClick = {
                            val vx = inputX.toDoubleOrNull() ?: return@IconButton
                            if (!isTwoVarMode) {
                                val freq = inputYOrFreq.toIntOrNull() ?: 1
                                val newList = oneVarData.toMutableList()
                                newList.add(Pair(vx, freq))
                                oneVarData = newList
                            } else {
                                val vy = inputYOrFreq.toDoubleOrNull() ?: 0.0
                                val newList = twoVarData.toMutableList()
                                newList.add(Pair(vx, vy))
                                twoVarData = newList
                            }
                            inputX = ""
                            inputYOrFreq = if (isTwoVarMode) "" else "1"
                        },
                        modifier = Modifier.testTag("stat_add_row_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = PrimaryCyan)
                    }
                }
            }
        }

        // Data Table List
        item {
            Text(
                text = if (isArabic) "جدول البيانات المدخلة:" else "Data Points:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }

        if (!isTwoVarMode) {
            itemsIndexed(oneVarData) { idx, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF151D24), RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${idx + 1}   x = ${item.first} ,  Freq = ${item.second}",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    IconButton(onClick = {
                        val m = oneVarData.toMutableList()
                        m.removeAt(idx)
                        oneVarData = m
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        } else {
            itemsIndexed(twoVarData) { idx, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF151D24), RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${idx + 1}   (x: ${item.first}, y: ${item.second})",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    IconButton(onClick = {
                        val m = twoVarData.toMutableList()
                        m.removeAt(idx)
                        twoVarData = m
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }

        // Summary Statistics Presentation Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                modifier = Modifier.fillMaxWidth().testTag("stat_results_card")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (isArabic) "المؤشرات الإحصائية الناتجة:" else "Statistical Summary:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (!isTwoVarMode && oneStats != null) {
                        val s = oneStats!!
                        StatRow("n (Sample Size)", s.n.toString())
                        StatRow("x̄ (Mean)", String.format("%.4f", s.mean))
                        StatRow("Σx", String.format("%.4f", s.sumX))
                        StatRow("Σx²", String.format("%.4f", s.sumX2))
                        StatRow("sx (Sample Std Dev)", String.format("%.4f", s.sampleStdDev))
                        StatRow("σx (Pop Std Dev)", String.format("%.4f", s.popStdDev))
                        StatRow("s²x (Sample Variance)", String.format("%.4f", s.sampleVariance))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFF263238))
                        StatRow("min(x)", String.format("%.4f", s.min))
                        StatRow("Q1 (First Quartile)", String.format("%.4f", s.q1))
                        StatRow("Med (Median)", String.format("%.4f", s.median))
                        StatRow("Q3 (Third Quartile)", String.format("%.4f", s.q3))
                        StatRow("max(x)", String.format("%.4f", s.max))
                    } else if (isTwoVarMode && twoStats != null) {
                        val s = twoStats!!
                        StatRow("Model", "y = a + bx")
                        StatRow("a (Intercept)", String.format("%.4f", s.a))
                        StatRow("b (Slope)", String.format("%.4f", s.b))
                        StatRow("r (Correlation)", String.format("%.4f", s.r))
                        StatRow("r² (Coeff Determination)", String.format("%.4f", s.r2))
                        StatRow("x̄", String.format("%.4f", s.meanX))
                        StatRow("ȳ", String.format("%.4f", s.meanY))
                        StatRow("Σxy", String.format("%.4f", s.sumXY))
                    } else {
                        Text(
                            text = if (isArabic) "أدخل البيانات لعرض النتائج" else "Enter data to view statistics",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Color(0xFFB0BEC5))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.White)
    }
}
