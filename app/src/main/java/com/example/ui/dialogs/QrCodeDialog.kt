package com.example.ui.dialogs

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.QrCodeGenerator
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun QrCodeDialog(
    payload: String,
    isArabic: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val qrMatrix = remember(payload) {
        try {
            QrCodeGenerator.encode(payload)
        } catch (_: Exception) {
            null
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("qr_code_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E242C),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryCyan)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "رمز QR للمشاركة والعرض" else "QR Code Result & Graph",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = KeyShiftGold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("qr_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // QR Code Canvas Render Box
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (qrMatrix != null) {
                        Canvas(modifier = Modifier.fillMaxSize().testTag("qr_code_canvas")) {
                            val moduleSize = size.width / qrMatrix.size
                            for (r in 0 until qrMatrix.size) {
                                for (c in 0 until qrMatrix.size) {
                                    if (qrMatrix[r, c]) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(c * moduleSize, r * moduleSize),
                                            size = Size(moduleSize, moduleSize)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = if (isArabic) "تعذر توليد الرمز" else "QR Generation Error",
                            color = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Explanatory note
                Text(
                    text = if (isArabic)
                        "امسح رمز QR بكاميرا هاتفك لعرض النتائج والبيانات والرسوم المدعومة، أو اضغط زر المشاركة أدناه."
                    else
                        "Scan this QR code with your phone camera to view equations, graph points, and formulas, or share directly.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFB0BEC5),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Summary payload preview
                Text(
                    text = payload.take(120) + (if (payload.length > 120) "..." else ""),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = PrimaryCyan,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Share intent button
                Button(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, payload)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Math Result")
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("qr_share_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "مشاركة النص والبيانات" else "Share Data",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
