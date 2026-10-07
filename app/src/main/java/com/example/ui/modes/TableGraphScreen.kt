package com.example.ui.modes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MathEvaluator
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan
import kotlin.math.roundToInt

@Composable
fun TableGraphScreen(
    evaluator: MathEvaluator,
    isArabic: Boolean,
    onShowQr: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fnExpr by remember { mutableStateOf("x^2 - 4") }
    var startVal by remember { mutableStateOf("-5") }
    var endVal by remember { mutableStateOf("5") }
    var stepVal by remember { mutableStateOf("1") }

    var tableData by remember { mutableStateOf<List<Pair<Double, Double>>>(emptyList()) }
    var showGraphTab by remember { mutableStateOf(false) }

    // Graph viewport state
    var zoomScale by remember { mutableFloatStateOf(35f) } // px per unit
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }
    var touchedPoint by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    fun generateTable() {
        val s = startVal.toDoubleOrNull() ?: -5.0
        val e = endVal.toDoubleOrNull() ?: 5.0
        val st = (stepVal.toDoubleOrNull() ?: 1.0).coerceAtLeast(0.1)

        val list = mutableListOf<Pair<Double, Double>>()
        var curr = s
        while (curr <= e + 1e-9) {
            val res = try {
                evaluator.evaluate(fnExpr, curr).decimalValue
            } catch (_: Exception) {
                Double.NaN
            }
            list.add(curr to res)
            curr += st
        }
        tableData = list
    }

    LaunchedEffect(Unit) {
        generateTable()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("table_graph_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isArabic) "جدول الدوال والرسم البياني" else "Function Table & Graph",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryCyan
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = {
                        val qrPayload = "f(x) = $fnExpr\nTable values:\n" +
                                tableData.take(10).joinToString("\n") { "(${it.first}, ${it.second})" }
                        onShowQr(qrPayload)
                    },
                    modifier = Modifier.testTag("table_qr_btn")
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = "QR Code", tint = KeyShiftGold)
                }
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                    modifier = Modifier.testTag("table_back_btn")
                ) {
                    Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Function Input f(x)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "f(x) =",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = KeyShiftGold
            )
            OutlinedTextField(
                value = fnExpr,
                onValueChange = { fnExpr = it },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_fn_expr")
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Range inputs: Start, End, Step
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                value = startVal,
                onValueChange = { startVal = it },
                label = { Text(if (isArabic) "البداية" else "Start") },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("input_start")
            )
            OutlinedTextField(
                value = endVal,
                onValueChange = { endVal = it },
                label = { Text(if (isArabic) "النهاية" else "End") },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("input_end")
            )
            OutlinedTextField(
                value = stepVal,
                onValueChange = { stepVal = it },
                label = { Text(if (isArabic) "الخطوة" else "Step") },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("input_step")
            )
            Button(
                onClick = { generateTable() },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag("btn_calc_table")
            ) {
                Text(if (isArabic) "تحديث" else "Gen", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Switch between Table and Graph view
        Row(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = if (showGraphTab) 1 else 0) {
                Tab(
                    selected = !showGraphTab,
                    onClick = { showGraphTab = false },
                    text = { Text(if (isArabic) "جدول القيم (Table)" else "Table Values") }
                )
                Tab(
                    selected = showGraphTab,
                    onClick = { showGraphTab = true },
                    text = { Text(if (isArabic) "الرسم البياني (Graph)" else "2D Function Graph") }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!showGraphTab) {
            // Table view
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF263238))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("x", fontWeight = FontWeight.Bold, color = KeyShiftGold, modifier = Modifier.weight(1f))
                        Text("f(x)", fontWeight = FontWeight.Bold, color = PrimaryCyan, modifier = Modifier.weight(1f))
                    }
                    HorizontalDivider(color = Color(0xFF37474F))

                    LazyColumn(modifier = Modifier.fillMaxSize().testTag("table_rows_list")) {
                        items(tableData) { (x, fx) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = String.format("%.2f", x),
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = if (fx.isNaN()) "Error" else String.format("%.4f", fx),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = if (fx.isNaN()) Color.Red else PrimaryCyan,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            HorizontalDivider(color = Color(0xFF26303C))
                        }
                    }
                }
            }
        } else {
            // 2D Cartesian Coordinate Function Graph Plotter
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F141C))
                    .testTag("function_graph_canvas")
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                panOffsetX += dragAmount.x
                                panOffsetY += dragAmount.y
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures { tapOffset ->
                                val originX = size.width / 2f + panOffsetX
                                val originY = size.height / 2f + panOffsetY
                                val mathX = (tapOffset.x - originX) / zoomScale
                                val mathY = try {
                                    evaluator.evaluate(fnExpr, mathX.toDouble()).decimalValue
                                } catch (_: Exception) { Double.NaN }
                                touchedPoint = if (!mathY.isNaN()) Pair(mathX.toDouble(), mathY) else null
                            }
                        }
                ) {
                    val originX = size.width / 2f + panOffsetX
                    val originY = size.height / 2f + panOffsetY

                    // 1. Draw Grid Lines
                    val gridColor = Color(0xFF212B36)
                    val unit = zoomScale
                    var x = originX % unit
                    while (x < size.width) {
                        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                        x += unit
                    }
                    var y = originY % unit
                    while (y < size.height) {
                        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                        y += unit
                    }

                    // 2. Draw Axes
                    val axisColor = Color(0xFF90A4AE)
                    drawLine(axisColor, Offset(0f, originY), Offset(size.width, originY), strokeWidth = 2f)
                    drawLine(axisColor, Offset(originX, 0f), Offset(originX, size.height), strokeWidth = 2f)

                    // 3. Plot Curve of f(x)
                    val path = Path()
                    var isFirst = true
                    var px = 0f
                    val stepPx = 2f

                    while (px <= size.width) {
                        val mathX = (px - originX) / zoomScale
                        val mathY = try {
                            evaluator.evaluate(fnExpr, mathX.toDouble()).decimalValue
                        } catch (_: Exception) {
                            Double.NaN
                        }

                        if (!mathY.isNaN() && !mathY.isInfinite()) {
                            val py = originY - (mathY.toFloat() * zoomScale)
                            if (py in -1000f..(size.height + 1000f)) {
                                if (isFirst) {
                                    path.moveTo(px, py)
                                    isFirst = false
                                } else {
                                    path.lineTo(px, py)
                                }
                            } else {
                                isFirst = true
                            }
                        } else {
                            isFirst = true
                        }
                        px += stepPx
                    }

                    drawPath(
                        path = path,
                        color = PrimaryCyan,
                        style = Stroke(width = 3.5f)
                    )

                    // 4. Draw touched point indicator if active
                    touchedPoint?.let { (tx, ty) ->
                        val ptX = originX + (tx.toFloat() * zoomScale)
                        val ptY = originY - (ty.toFloat() * zoomScale)
                        drawCircle(color = KeyShiftGold, radius = 6f, center = Offset(ptX, ptY))
                    }
                }

                // Zoom & Reset controls overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FloatingActionButton(
                        onClick = { zoomScale = (zoomScale * 1.25f).coerceAtMost(250f) },
                        modifier = Modifier.size(36.dp),
                        containerColor = KeyFunctionBg
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color.White)
                    }
                    FloatingActionButton(
                        onClick = { zoomScale = (zoomScale / 1.25f).coerceAtLeast(8f) },
                        modifier = Modifier.size(36.dp),
                        containerColor = KeyFunctionBg
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.White)
                    }
                    FloatingActionButton(
                        onClick = {
                            zoomScale = 35f
                            panOffsetX = 0f
                            panOffsetY = 0f
                            touchedPoint = null
                        },
                        modifier = Modifier.size(36.dp),
                        containerColor = KeyFunctionBg
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Pan", tint = Color.White)
                    }
                }

                // Touched Coordinate Badge
                touchedPoint?.let { (tx, ty) ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(8.dp),
                        color = Color(0xCC000000),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "(x: ${String.format("%.2f", tx)}, y: ${String.format("%.2f", ty)})",
                            color = KeyShiftGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
