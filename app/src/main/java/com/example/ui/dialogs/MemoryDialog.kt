package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.KeyAlphaPink
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun MemoryDialog(
    variables: Map<String, Double>,
    ans: Double,
    preAns: Double,
    isArabic: Boolean,
    onRecall: (String) -> Unit,
    onStore: (String) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val varList = listOf("M", "A", "B", "C", "D", "E", "F", "X", "Y")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(12.dp)
                .testTag("memory_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E242C),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF37474F))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "إدارة الذاكرة والمتغيرات (MEMORY)" else "MEMORY & VARIABLES",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = KeyShiftGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Row {
                        IconButton(onClick = onClearAll, modifier = Modifier.testTag("mem_clear_btn")) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All", tint = Color.Red)
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("mem_close_btn")) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ans and PreAns summary cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF101419)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Ans (Last Result)", fontSize = 11.sp, color = PrimaryCyan)
                            Text(
                                text = ans.toString(),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF101419)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("PreAns (Previous)", fontSize = 11.sp, color = KeyShiftGold)
                            Text(
                                text = preAns.toString(),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Variables Table
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(varList) { vName ->
                        val valDouble = variables[vName] ?: 0.0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF161E28), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = vName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (vName == "M") KeyShiftGold else KeyAlphaPink
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "= $valDouble",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Button(
                                    onClick = {
                                        onRecall(vName)
                                        onDismiss()
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp).testTag("mem_rcl_$vName")
                                ) {
                                    Text("RCL", fontSize = 10.sp)
                                }
                                OutlinedButton(
                                    onClick = {
                                        onStore(vName)
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp).testTag("mem_sto_$vName")
                                ) {
                                    Text("STO", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
