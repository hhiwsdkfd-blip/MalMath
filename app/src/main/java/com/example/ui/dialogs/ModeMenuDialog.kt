package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CalculatorMode
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

data class ModeItemData(
    val mode: CalculatorMode,
    val icon: ImageVector,
    val number: String
)

@Composable
fun ModeMenuDialog(
    currentMode: CalculatorMode,
    isArabic: Boolean,
    onSelectMode: (CalculatorMode) -> Unit,
    onDismiss: () -> Unit
) {
    val items = listOf(
        ModeItemData(CalculatorMode.CALCULATE, Icons.Default.Calculate, "1"),
        ModeItemData(CalculatorMode.COMPLEX, Icons.Default.Hub, "2"),
        ModeItemData(CalculatorMode.BASE_N, Icons.Default.Numbers, "3"),
        ModeItemData(CalculatorMode.MATRIX, Icons.Default.GridOn, "4"),
        ModeItemData(CalculatorMode.VECTOR, Icons.Default.TrendingUp, "5"),
        ModeItemData(CalculatorMode.STATISTICS, Icons.Default.BarChart, "6"),
        ModeItemData(CalculatorMode.TABLE, Icons.Default.TableChart, "7"),
        ModeItemData(CalculatorMode.EQUATION, Icons.Default.Functions, "8"),
        ModeItemData(CalculatorMode.UNIT_CONVERTER, Icons.Default.SwapHoriz, "9")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("mode_menu_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E242C),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF37474F))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "قائمة الأوضاع (MENU / SETUP)" else "MODE MENU",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = KeyShiftGold,
                        fontFamily = FontFamily.Monospace
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("mode_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items) { item ->
                        val isSelected = currentMode == item.mode
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onSelectMode(item.mode)
                                    onDismiss()
                                }
                                .testTag("mode_item_${item.mode.name}"),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF004D73) else Color(0xFF26303C),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 0.5.dp,
                                if (isSelected) PrimaryCyan else Color(0xFF37474F)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isSelected) PrimaryCyan else Color(0xFF37474F)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.number,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) Color.Black else Color.White
                                    )
                                }
                                Column {
                                    Text(
                                        text = if (isArabic) item.mode.nameAr else item.mode.nameEn,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = item.mode.shortIndicator,
                                        fontSize = 9.sp,
                                        color = KeyShiftGold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
