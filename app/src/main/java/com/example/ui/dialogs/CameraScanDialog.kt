package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun CameraScanDialog(
    isArabic: Boolean,
    onInsertExpression: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sampleScans = listOf(
        Pair("∫(0, π) sin(x) dx", "integrate(sin(x), 0, pi)"),
        Pair("d/dx (x³ - 5x) at x=2", "diff(x^3 - 5*x, 2)"),
        Pair("3/4 + 5/6 - 1/2", "3/4 + 5/6 - 1/2"),
        Pair("√144 + ∛27", "sqrt(144) + cbrt(27)"),
        Pair("sin(30°) + cos(60°)", "sin(30) + cos(60)"),
        Pair("log(100) + ln(e²)", "log(100) + ln(e^2)"),
        Pair("nPr(5, 2) + nCr(6, 3)", "nPr(5, 2) + nCr(6, 3)"),
        Pair("x² - 4x + 3 = 0", "x^2 - 4*x + 3")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(12.dp)
                .testTag("camera_scan_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E242C),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryCyan)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = PrimaryCyan)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "المسح الضوئي للمسائل (Photo Math)" else "Math Camera Scanner",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("camera_close_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Simulated Viewfinder Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F141C))
                        .border(2.dp, PrimaryCyan, RoundedCornerShape(12.dp))
                        .testTag("camera_viewfinder"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PhotoLibrary,
                            contentDescription = "Scan Target",
                            tint = KeyShiftGold,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isArabic) "وجه الكاميرا نحو المعادلة في الكتاب المدرسي" else "Aim camera at equation in textbook",
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (isArabic) "[ تعرف ضوئي ذكي OCR ]" else "[ Smart OCR Recognition ]",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "نماذج مسائل سريعة للتعرف والحل الفوري:" else "Sample problems for instant recognition:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = KeyShiftGold
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(sampleScans) { (displayTxt, evalExpr) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF141A22)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onInsertExpression(evalExpr)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = displayTxt,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Button(
                                    onClick = {
                                        onInsertExpression(evalExpr)
                                        onDismiss()
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(if (isArabic) "حل الآن" else "Solve", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
