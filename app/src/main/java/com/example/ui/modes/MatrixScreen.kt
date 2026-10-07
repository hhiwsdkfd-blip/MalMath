package com.example.ui.modes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Matrix
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun MatrixScreen(
    matrices: Map<String, Matrix>,
    onUpdateMatrix: (String, Matrix) -> Unit,
    isArabic: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMatName by remember { mutableStateOf("MatA") }
    var currentMatrix by remember(selectedMatName) {
        mutableStateOf(matrices[selectedMatName] ?: Matrix(2, 2, arrayOf(doubleArrayOf(1.0, 0.0), doubleArrayOf(0.0, 1.0))))
    }
    var rows by remember(selectedMatName) { mutableIntStateOf(currentMatrix.rows) }
    var cols by remember(selectedMatName) { mutableIntStateOf(currentMatrix.cols) }
    var resultText by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("matrix_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            // Header with Matrix Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "محرر وعمليات المصفوفات" else "Matrix Operations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                    modifier = Modifier.testTag("matrix_back_btn")
                ) {
                    Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                }
            }
        }

        item {
            // Matrix selector tabs: MatA, MatB, MatC
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("MatA", "MatB", "MatC").forEach { name ->
                    FilterChip(
                        selected = selectedMatName == name,
                        onClick = {
                            selectedMatName = name
                            resultText = null
                        },
                        label = { Text(name, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f).testTag("select_$name")
                    )
                }
            }
        }

        item {
            // Dimension selector: 2x2, 3x3, 4x4, 2x3, 3x2
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isArabic) "الأبعاد (${rows}×${cols}):" else "Dimensions (${rows}x${cols}):",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(Pair(2, 2), Pair(3, 3), Pair(4, 4), Pair(2, 3), Pair(3, 2)).forEach { (r, c) ->
                        OutlinedButton(
                            onClick = {
                                rows = r
                                cols = c
                                val newMat = Matrix(r, c, Array(r) { rowIdx ->
                                    DoubleArray(c) { colIdx ->
                                        if (rowIdx < currentMatrix.rows && colIdx < currentMatrix.cols) currentMatrix[rowIdx, colIdx] else 0.0
                                    }
                                })
                                currentMatrix = newMat
                                onUpdateMatrix(selectedMatName, newMat)
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("${r}×${c}", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        item {
            // Visual Matrix Grid Editor
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                modifier = Modifier.fillMaxWidth().testTag("matrix_grid")
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$selectedMatName [${rows}×${cols}]",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = KeyShiftGold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    for (r in 0 until rows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for (c in 0 until cols) {
                                var cellVal by remember(currentMatrix, r, c) {
                                    mutableStateOf(currentMatrix[r, c].let {
                                        if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString()
                                    })
                                }
                                OutlinedTextField(
                                    value = cellVal,
                                    onValueChange = { newVal ->
                                        cellVal = newVal
                                        val d = newVal.toDoubleOrNull() ?: 0.0
                                        currentMatrix.set(r, c, d)
                                        onUpdateMatrix(selectedMatName, currentMatrix)
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 2.dp)
                                        .testTag("cell_${r}_$c")
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // Matrix Operations Buttons
            Text(
                text = if (isArabic) "العمليات الحسابية" else "Operations",
                fontSize = 14.sp,
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
                        resultText = if (currentMatrix.isSquare) {
                            "det($selectedMatName) = ${currentMatrix.determinant()}"
                        } else {
                            if (isArabic) "المحدد معرف للمصفوفات المربعة فقط" else "Determinant requires square matrix"
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_det")
                ) {
                    Text("det", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        if (!currentMatrix.isSquare) {
                            resultText = if (isArabic) "المعكوس للمصفوفات المربعة فقط" else "Inverse requires square matrix"
                            return@Button
                        }
                        val inv = currentMatrix.inverse()
                        resultText = if (inv != null) {
                            "$selectedMatName⁻¹ =\n${inv.format()}"
                        } else {
                            if (isArabic) "مصفوفة منفردة (لا يوجد معكوس)" else "Singular matrix (no inverse)"
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_inv")
                ) {
                    Text("A⁻¹", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val tr = currentMatrix.transpose()
                        resultText = "${selectedMatName}ᵀ =\n${tr.format()}"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_transpose")
                ) {
                    Text("Aᵀ", fontSize = 12.sp)
                }

                if (currentMatrix.isSquare) {
                    Button(
                        onClick = {
                            val tr = currentMatrix.trace
                            resultText = "Tr($selectedMatName) = $tr"
                        },
                        modifier = Modifier.weight(1f).testTag("btn_trace")
                    ) {
                        Text("Tr", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Arithmetic Operations with other matrices
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        val matB = matrices["MatB"] ?: currentMatrix
                        try {
                            val res = currentMatrix + matB
                            resultText = "MatA + MatB =\n${res.format()}"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_add_mat")
                ) {
                    Text("A + B", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val matB = matrices["MatB"] ?: currentMatrix
                        try {
                            val res = currentMatrix * matB
                            resultText = "MatA × MatB =\n${res.format()}"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_mul_mat")
                ) {
                    Text("A × B", fontSize = 12.sp)
                }

                if (currentMatrix.isSquare) {
                    Button(
                        onClick = {
                            val res = currentMatrix.power(2)
                            resultText = "${selectedMatName}² =\n${res.format()}"
                        },
                        modifier = Modifier.weight(1f).testTag("btn_sqr_mat")
                    ) {
                        Text("A²", fontSize = 12.sp)
                    }
                }
            }
        }

        // Calculation Result Card
        if (resultText != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                    modifier = Modifier.fillMaxWidth().testTag("matrix_result_card")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isArabic) "النتيجة:" else "Result:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = resultText ?: "",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
