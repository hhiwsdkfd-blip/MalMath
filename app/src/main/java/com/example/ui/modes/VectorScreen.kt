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
import com.example.model.MathVector
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun VectorScreen(
    vectors: Map<String, MathVector>,
    onUpdateVector: (String, MathVector) -> Unit,
    isArabic: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedVctName by remember { mutableStateOf("VctA") }
    var currentVector by remember(selectedVctName) {
        mutableStateOf(vectors[selectedVctName] ?: MathVector(doubleArrayOf(1.0, 2.0, 3.0)))
    }
    var dim by remember(selectedVctName) { mutableIntStateOf(currentVector.dim) }
    var resultText by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("vector_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "محرر وعمليات المتجهات" else "Vector Operations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                    modifier = Modifier.testTag("vector_back_btn")
                ) {
                    Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("VctA", "VctB", "VctC").forEach { name ->
                    FilterChip(
                        selected = selectedVctName == name,
                        onClick = {
                            selectedVctName = name
                            resultText = null
                        },
                        label = { Text(name, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f).testTag("select_$name")
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isArabic) "الأبعاد (${dim}D):" else "Dimension (${dim}D):",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(2, 3, 4).forEach { d ->
                        OutlinedButton(
                            onClick = {
                                dim = d
                                val newVct = MathVector(DoubleArray(d) { idx ->
                                    if (idx < currentVector.dim) currentVector[idx] else 0.0
                                })
                                currentVector = newVct
                                onUpdateVector(selectedVctName, newVct)
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("${d}D", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "$selectedVctName [${dim}D]",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = KeyShiftGold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val labels = listOf("x", "y", "z", "w")
                        for (i in 0 until dim) {
                            var compVal by remember(currentVector, i) {
                                mutableStateOf(currentVector[i].let {
                                    if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString()
                                })
                            }
                            OutlinedTextField(
                                value = compVal,
                                onValueChange = { newVal ->
                                    compVal = newVal
                                    val d = newVal.toDoubleOrNull() ?: 0.0
                                    val newArr = currentVector.components.clone()
                                    newArr[i] = d
                                    val v = MathVector(newArr)
                                    currentVector = v
                                    onUpdateVector(selectedVctName, v)
                                },
                                label = { Text(labels.getOrElse(i) { "v$i" }) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("vct_comp_$i")
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = if (isArabic) "العمليات" else "Operations",
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
                        resultText = "|${selectedVctName}| = ${currentVector.norm}"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_norm")
                ) {
                    Text("|v|", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        try {
                            val u = currentVector.unitVector
                            resultText = "Unit($selectedVctName) = ${u.format()}"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_unit_vector")
                ) {
                    Text("Unit v", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val vctB = vectors["VctB"] ?: currentVector
                        try {
                            val d = currentVector.dot(vctB)
                            resultText = "$selectedVctName • VctB = $d"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_dot")
                ) {
                    Text("u • v", fontSize = 12.sp)
                }

                if (dim == 3) {
                    Button(
                        onClick = {
                            val vctB = vectors["VctB"] ?: currentVector
                            try {
                                val c = currentVector.cross(vctB)
                                resultText = "$selectedVctName × VctB = ${c.format()}"
                            } catch (e: Exception) {
                                resultText = e.message
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("btn_cross")
                    ) {
                        Text("u × v", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        val vctB = vectors["VctB"] ?: currentVector
                        try {
                            val angle = currentVector.angleDeg(vctB)
                            resultText = "Angle($selectedVctName, VctB) = $angle°"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_angle")
                ) {
                    Text(if (isArabic) "الزاوية (Angle)" else "Angle", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val vctB = vectors["VctB"] ?: currentVector
                        try {
                            val sum = currentVector + vctB
                            resultText = "$selectedVctName + VctB = ${sum.format()}"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_add_vct")
                ) {
                    Text("u + v", fontSize = 12.sp)
                }
            }
        }

        if (resultText != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                    modifier = Modifier.fillMaxWidth().testTag("vct_result_card")
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
