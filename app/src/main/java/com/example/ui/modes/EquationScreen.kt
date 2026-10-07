package com.example.ui.modes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.example.model.EquationResult
import com.example.model.EquationSolver
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

enum class EqType(val labelEn: String, val labelAr: String) {
    SIMULT_2("Simultaneous 2 Unknowns", "نظام خطي بمجهولين (2 Unknowns)"),
    SIMULT_3("Simultaneous 3 Unknowns", "نظام خطي بـ 3 مجاهيل (3 Unknowns)"),
    POLY_2("Polynomial Degree 2 (ax²+bx+c=0)", "معادلة تربيعية (الدرجة الثانية)"),
    POLY_3("Polynomial Degree 3 (ax³+...+d=0)", "معادلة تكعيبية (الدرجة الثالثة)"),
    POLY_4("Polynomial Degree 4 (ax⁴+...+e=0)", "معادلة من الدرجة الرابعة")
}

@Composable
fun EquationScreen(
    isArabic: Boolean,
    onShowQr: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(EqType.POLY_2) }

    // Quadratic inputs
    var a2 by remember { mutableStateOf("1") }
    var b2 by remember { mutableStateOf("-5") }
    var c2 by remember { mutableStateOf("6") }

    // Cubic inputs
    var a3 by remember { mutableStateOf("1") }
    var b3 by remember { mutableStateOf("-6") }
    var c3 by remember { mutableStateOf("11") }
    var d3 by remember { mutableStateOf("-6") }

    // Quartic inputs
    var a4 by remember { mutableStateOf("1") }
    var b4 by remember { mutableStateOf("0") }
    var c4 by remember { mutableStateOf("-5") }
    var d4 by remember { mutableStateOf("0") }
    var e4 by remember { mutableStateOf("4") }

    // 2x2 Linear inputs: a1 x + b1 y = c1
    var l2_a1 by remember { mutableStateOf("2") }
    var l2_b1 by remember { mutableStateOf("3") }
    var l2_c1 by remember { mutableStateOf("8") }
    var l2_a2 by remember { mutableStateOf("1") }
    var l2_b2 by remember { mutableStateOf("-1") }
    var l2_c2 by remember { mutableStateOf("-1") }

    // 3x3 Linear inputs
    var l3_r1 by remember { mutableStateOf(listOf("1", "1", "1", "6")) }
    var l3_r2 by remember { mutableStateOf(listOf("0", "2", "5", "-4")) }
    var l3_r3 by remember { mutableStateOf(listOf("2", "5", "-1", "27")) }

    var solution by remember { mutableStateOf<EquationResult?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("equation_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "محلل المعادلات الرياضية" else "Equation Solver",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                    modifier = Modifier.testTag("eqn_back_btn")
                ) {
                    Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                }
            }
        }

        item {
            // Type dropdown or chips
            Column {
                Text(
                    text = if (isArabic) "اختر نوع المعادلة:" else "Select Equation Type:",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                EqType.values().forEach { eq ->
                    FilterChip(
                        selected = selectedType == eq,
                        onClick = {
                            selectedType = eq
                            solution = null
                        },
                        label = { Text(if (isArabic) eq.labelAr else eq.labelEn, fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .testTag("eqn_chip_${eq.name}")
                    )
                }
            }
        }

        // Input Fields based on type
        when (selectedType) {
            EqType.POLY_2 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "a x² + b x + c = 0",
                                fontWeight = FontWeight.Bold,
                                color = KeyShiftGold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(value = a2, onValueChange = { a2 = it }, label = { Text("a") }, modifier = Modifier.weight(1f).testTag("poly2_a"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = b2, onValueChange = { b2 = it }, label = { Text("b") }, modifier = Modifier.weight(1f).testTag("poly2_b"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = c2, onValueChange = { c2 = it }, label = { Text("c") }, modifier = Modifier.weight(1f).testTag("poly2_c"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            }
                        }
                    }
                }
            }
            EqType.POLY_3 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "a x³ + b x² + c x + d = 0",
                                fontWeight = FontWeight.Bold,
                                color = KeyShiftGold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                OutlinedTextField(value = a3, onValueChange = { a3 = it }, label = { Text("a") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = b3, onValueChange = { b3 = it }, label = { Text("b") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = c3, onValueChange = { c3 = it }, label = { Text("c") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = d3, onValueChange = { d3 = it }, label = { Text("d") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            }
                        }
                    }
                }
            }
            EqType.POLY_4 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "a x⁴ + b x³ + c x² + d x + e = 0",
                                fontWeight = FontWeight.Bold,
                                color = KeyShiftGold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                OutlinedTextField(value = a4, onValueChange = { a4 = it }, label = { Text("a") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = b4, onValueChange = { b4 = it }, label = { Text("b") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = c4, onValueChange = { c4 = it }, label = { Text("c") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = d4, onValueChange = { d4 = it }, label = { Text("d") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = e4, onValueChange = { e4 = it }, label = { Text("e") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            }
                        }
                    }
                }
            }
            EqType.SIMULT_2 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("a₁ x + b₁ y = c₁", color = KeyShiftGold, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(value = l2_a1, onValueChange = { l2_a1 = it }, label = { Text("a₁") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = l2_b1, onValueChange = { l2_b1 = it }, label = { Text("b₁") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = l2_c1, onValueChange = { l2_c1 = it }, label = { Text("c₁") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("a₂ x + b₂ y = c₂", color = KeyShiftGold, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(value = l2_a2, onValueChange = { l2_a2 = it }, label = { Text("a₂") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = l2_b2, onValueChange = { l2_b2 = it }, label = { Text("b₂") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                OutlinedTextField(value = l2_c2, onValueChange = { l2_c2 = it }, label = { Text("c₂") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            }
                        }
                    }
                }
            }
            EqType.SIMULT_3 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("a x + b y + c z = d", color = KeyShiftGold, fontWeight = FontWeight.Bold)
                            // 3 rows
                            listOf(
                                Triple("Row 1", l3_r1, { l: List<String> -> l3_r1 = l }),
                                Triple("Row 2", l3_r2, { l: List<String> -> l3_r2 = l }),
                                Triple("Row 3", l3_r3, { l: List<String> -> l3_r3 = l })
                            ).forEach { (rLabel, rVals, updater) ->
                                Text(rLabel, fontSize = 11.sp, color = Color.Gray)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    for (cIdx in 0..3) {
                                        OutlinedTextField(
                                            value = rVals[cIdx],
                                            onValueChange = { newVal ->
                                                val m = rVals.toMutableList()
                                                m[cIdx] = newVal
                                                updater(m)
                                            },
                                            label = { Text(listOf("a", "b", "c", "d")[cIdx]) },
                                            modifier = Modifier.weight(1f),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }

        // Solve button
        item {
            Button(
                onClick = {
                    solution = when (selectedType) {
                        EqType.POLY_2 -> EquationSolver.solveQuadratic(
                            a2.toDoubleOrNull() ?: 1.0,
                            b2.toDoubleOrNull() ?: 0.0,
                            c2.toDoubleOrNull() ?: 0.0
                        )
                        EqType.POLY_3 -> EquationSolver.solveCubic(
                            a3.toDoubleOrNull() ?: 1.0,
                            b3.toDoubleOrNull() ?: 0.0,
                            c3.toDoubleOrNull() ?: 0.0,
                            d3.toDoubleOrNull() ?: 0.0
                        )
                        EqType.POLY_4 -> EquationSolver.solveQuartic(
                            a4.toDoubleOrNull() ?: 1.0,
                            b4.toDoubleOrNull() ?: 0.0,
                            c4.toDoubleOrNull() ?: 0.0,
                            d4.toDoubleOrNull() ?: 0.0,
                            e4.toDoubleOrNull() ?: 0.0
                        )
                        EqType.SIMULT_2 -> {
                            val coeff = arrayOf(
                                doubleArrayOf(l2_a1.toDoubleOrNull() ?: 1.0, l2_b1.toDoubleOrNull() ?: 1.0),
                                doubleArrayOf(l2_a2.toDoubleOrNull() ?: 1.0, l2_b2.toDoubleOrNull() ?: 1.0)
                            )
                            val consts = doubleArrayOf(l2_c1.toDoubleOrNull() ?: 0.0, l2_c2.toDoubleOrNull() ?: 0.0)
                            EquationSolver.solveLinearSystem(coeff, consts)
                        }
                        EqType.SIMULT_3 -> {
                            val coeff = arrayOf(
                                doubleArrayOf(l3_r1[0].toDoubleOrNull() ?: 1.0, l3_r1[1].toDoubleOrNull() ?: 1.0, l3_r1[2].toDoubleOrNull() ?: 1.0),
                                doubleArrayOf(l3_r2[0].toDoubleOrNull() ?: 1.0, l3_r2[1].toDoubleOrNull() ?: 1.0, l3_r2[2].toDoubleOrNull() ?: 1.0),
                                doubleArrayOf(l3_r3[0].toDoubleOrNull() ?: 1.0, l3_r3[1].toDoubleOrNull() ?: 1.0, l3_r3[2].toDoubleOrNull() ?: 1.0)
                            )
                            val consts = doubleArrayOf(
                                l3_r1[3].toDoubleOrNull() ?: 0.0,
                                l3_r2[3].toDoubleOrNull() ?: 0.0,
                                l3_r3[3].toDoubleOrNull() ?: 0.0
                            )
                            EquationSolver.solveLinearSystem(coeff, consts)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_solve_equation")
            ) {
                Text(if (isArabic) "حساب الجذور والحلول [SOLVE]" else "Solve Equation [SOLVE]", fontWeight = FontWeight.Bold)
            }
        }

        // Solution presentation Card
        if (solution != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                    modifier = Modifier.fillMaxWidth().testTag("equation_solution_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabic) "الحلول والجذور (Solutions):" else "Solutions & Roots:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryCyan
                            )
                            IconButton(
                                onClick = {
                                    val summary = formatSolutionSummary(selectedType, solution!!)
                                    onShowQr(summary)
                                },
                                modifier = Modifier.testTag("eqn_qr_btn")
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = "QR Code", tint = KeyShiftGold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        when (val sol = solution) {
                            is EquationResult.PolynomialSolution -> {
                                sol.roots.forEachIndexed { index, root ->
                                    Text(
                                        text = "x${index + 1} = ${root.toRectangularString()}",
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                }
                                if (sol.extrema.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (isArabic) "رأس المنحنى (Vertex / Min-Max):" else "Vertex (Min/Max):",
                                        fontSize = 12.sp,
                                        color = KeyShiftGold
                                    )
                                    sol.extrema.forEach { (vx, vy) ->
                                        Text(
                                            text = "x = $vx , y = $vy",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                            is EquationResult.LinearSolution -> {
                                sol.variables.forEach { (name, value) ->
                                    Text(
                                        text = "$name = $value",
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                }
                            }
                            is EquationResult.NoSolution -> {
                                Text(
                                    text = if (isArabic) "لا يوجد حل (No Solution)" else "No Solution",
                                    color = Color(0xFFEF5350),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            is EquationResult.InfiniteSolutions -> {
                                Text(
                                    text = if (isArabic) "عدد لا نهائي من الحلول (Infinite Solutions)" else "Infinite Solutions",
                                    color = KeyShiftGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            is EquationResult.Error -> {
                                Text(text = sol.message, color = Color(0xFFEF5350))
                            }
                            null -> {}
                        }
                    }
                }
            }
        }
    }
}

private fun formatSolutionSummary(type: EqType, sol: EquationResult): String {
    val sb = StringBuilder("Equation: ${type.name}\n")
    when (sol) {
        is EquationResult.PolynomialSolution -> {
            sol.roots.forEachIndexed { i, r ->
                sb.append("x${i + 1} = ${r.toRectangularString()}\n")
            }
        }
        is EquationResult.LinearSolution -> {
            sol.variables.forEach { (name, v) ->
                sb.append("$name = $v\n")
            }
        }
        is EquationResult.NoSolution -> sb.append("No Solution")
        is EquationResult.InfiniteSolutions -> sb.append("Infinite Solutions")
        is EquationResult.Error -> sb.append("Error: ${sol.message}")
    }
    return sb.toString().trim()
}
