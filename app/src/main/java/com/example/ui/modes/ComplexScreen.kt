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
import com.example.model.ComplexNumber
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun ComplexScreen(
    isArabic: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var re1 by remember { mutableStateOf("3") }
    var im1 by remember { mutableStateOf("4") }

    var re2 by remember { mutableStateOf("1") }
    var im2 by remember { mutableStateOf("-2") }

    val z1 = remember(re1, im1) {
        ComplexNumber(re1.toDoubleOrNull() ?: 0.0, im1.toDoubleOrNull() ?: 0.0)
    }
    val z2 = remember(re2, im2) {
        ComplexNumber(re2.toDoubleOrNull() ?: 0.0, im2.toDoubleOrNull() ?: 0.0)
    }

    var resultText by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("complex_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الأعداد المركبة (CMPLX)" else "Complex Numbers (CMPLX)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KeyFunctionBg),
                    modifier = Modifier.testTag("cmplx_back_btn")
                ) {
                    Text(if (isArabic) "إغلاق" else "Done", fontSize = 12.sp)
                }
            }
        }

        // z1 input
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("z₁ = a + bi", fontWeight = FontWeight.Bold, color = KeyShiftGold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = re1,
                            onValueChange = { re1 = it },
                            label = { Text("Re (a)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("z1_re"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = im1,
                            onValueChange = { im1 = it },
                            label = { Text("Im (b)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("z1_im"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Polar: ${z1.toPolarString(true)}",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // z1 single operations
        item {
            Text(
                text = if (isArabic) "خصائص z₁:" else "z₁ Properties & Operations:",
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
                        resultText = "|z₁| = ${z1.abs}\n(Modulus / المقياس)"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_abs")
                ) {
                    Text("|z|", fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        resultText = "arg(z₁) = ${z1.argDeg}°  (${z1.argRad} rad)\n(Argument / السعة)"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_arg")
                ) {
                    Text("arg(z)", fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        resultText = "Conjugate(z₁) = ${z1.conjugate.toRectangularString()}\n(المرافق)"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_conj")
                ) {
                    Text("z*", fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        resultText = "√z₁ = ${z1.sqrt().toRectangularString()}"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_sqrt_z")
                ) {
                    Text("√z", fontSize = 12.sp)
                }
            }
        }

        // z2 input
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("z₂ = c + di", fontWeight = FontWeight.Bold, color = KeyShiftGold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = re2,
                            onValueChange = { re2 = it },
                            label = { Text("Re (c)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("z2_re"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = im2,
                            onValueChange = { im2 = it },
                            label = { Text("Im (d)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("z2_im"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Polar: ${z2.toPolarString(true)}",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Binary operations between z1 and z2
        item {
            Text(
                text = if (isArabic) "العمليات بين z₁ و z₂:" else "Operations between z₁ and z₂:",
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
                        val res = z1 + z2
                        resultText = "z₁ + z₂ = ${res.toRectangularString()}\nPolar: ${res.toPolarString(true)}"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_add_z")
                ) {
                    Text("z₁ + z₂", fontSize = 11.sp)
                }
                Button(
                    onClick = {
                        val res = z1 - z2
                        resultText = "z₁ - z₂ = ${res.toRectangularString()}\nPolar: ${res.toPolarString(true)}"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_sub_z")
                ) {
                    Text("z₁ - z₂", fontSize = 11.sp)
                }
                Button(
                    onClick = {
                        val res = z1 * z2
                        resultText = "z₁ × z₂ = ${res.toRectangularString()}\nPolar: ${res.toPolarString(true)}"
                    },
                    modifier = Modifier.weight(1f).testTag("btn_mul_z")
                ) {
                    Text("z₁ × z₂", fontSize = 11.sp)
                }
                Button(
                    onClick = {
                        try {
                            val res = z1 / z2
                            resultText = "z₁ ÷ z₂ = ${res.toRectangularString()}\nPolar: ${res.toPolarString(true)}"
                        } catch (e: Exception) {
                            resultText = e.message
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_div_z")
                ) {
                    Text("z₁ ÷ z₂", fontSize = 11.sp)
                }
            }
        }

        // Result Card
        if (resultText != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                    modifier = Modifier.fillMaxWidth().testTag("complex_result_card")
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
