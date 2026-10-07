package com.example.ui.modes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.ConversionUnit
import com.example.model.UnitCategory
import com.example.model.UnitConverterData
import com.example.ui.theme.KeyFunctionBg
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

data class ConverterHistoryItem(
    val category: UnitCategory,
    val fromValue: Double,
    val fromUnit: ConversionUnit,
    val toValue: Double,
    val toUnit: ConversionUnit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    isArabic: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(UnitCategory.LENGTH) }
    var activeTab by remember { mutableIntStateOf(0) } // 0: Converter, 1: Favorites, 2: History

    val unitsForCat = remember(selectedCategory) {
        UnitConverterData.categories[selectedCategory] ?: emptyList()
    }

    var fromUnit by remember(selectedCategory) {
        mutableStateOf(unitsForCat.firstOrNull() ?: ConversionUnit("", "", "", "", 1.0))
    }
    var toUnit by remember(selectedCategory) {
        mutableStateOf(unitsForCat.getOrNull(1) ?: unitsForCat.firstOrNull() ?: ConversionUnit("", "", "", "", 1.0))
    }

    var inputValue by remember { mutableStateOf("1") }
    var favoriteCategories by remember { mutableStateOf(setOf(UnitCategory.LENGTH, UnitCategory.CURRENCY, UnitCategory.TIME)) }
    var historyList by remember { mutableStateOf(mutableListOf<ConverterHistoryItem>()) }

    val numericInput = inputValue.toDoubleOrNull() ?: 0.0
    val convertedResult = remember(numericInput, fromUnit, toUnit, selectedCategory) {
        UnitConverterData.convert(numericInput, fromUnit, toUnit, selectedCategory)
    }

    fun saveToHistory() {
        if (historyList.none { it.fromValue == numericInput && it.fromUnit == fromUnit && it.toUnit == toUnit }) {
            historyList.add(0, ConverterHistoryItem(selectedCategory, numericInput, fromUnit, convertedResult, toUnit))
        }
    }

    LaunchedEffect(convertedResult) {
        if (numericInput > 0) saveToHistory()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("unit_converter_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("conv_back_btn")) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isArabic) "محول الوحدات الشامل" else "Unit Converter",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
            }

            // Favorite star toggle
            IconButton(
                onClick = {
                    favoriteCategories = if (favoriteCategories.contains(selectedCategory)) {
                        favoriteCategories - selectedCategory
                    } else {
                        favoriteCategories + selectedCategory
                    }
                },
                modifier = Modifier.testTag("conv_fav_toggle")
            ) {
                Icon(
                    imageVector = if (favoriteCategories.contains(selectedCategory)) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Favorite",
                    tint = KeyShiftGold
                )
            }
        }

        // Sub Tabs: Converter | Favorites | History
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color(0xFF161C24)
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text(if (isArabic) "المحول" else "Converter", fontSize = 12.sp) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = { Text(if (isArabic) "المفضلة ⭐" else "Favorites ⭐", fontSize = 12.sp) }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = { Text(if (isArabic) "السجل" else "History", fontSize = 12.sp) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (activeTab) {
            0, 1 -> {
                // Horizontal category chips
                val categoriesToShow = if (activeTab == 1) {
                    UnitCategory.values().filter { favoriteCategories.contains(it) }
                } else {
                    UnitCategory.values().toList()
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesToShow.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = {
                                selectedCategory = cat
                                val units = UnitConverterData.categories[cat] ?: emptyList()
                                fromUnit = units.firstOrNull() ?: fromUnit
                                toUnit = units.getOrNull(1) ?: units.firstOrNull() ?: toUnit
                            },
                            label = { Text(if (isArabic) cat.nameAr else cat.nameEn, fontSize = 11.sp) },
                            modifier = Modifier.testTag("cat_chip_${cat.name}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input Value Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabic) "القيمة المدخلة:" else "Input Value:",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                            Text(
                                text = "${fromUnit.symbol} ➜ ${toUnit.symbol}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = KeyShiftGold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("conv_input_val"),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // From / To Unit Selectors with Swap button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // From Unit Dropdown
                            UnitDropdownMenu(
                                selectedUnit = fromUnit,
                                allUnits = unitsForCat,
                                isArabic = isArabic,
                                onSelect = { fromUnit = it },
                                modifier = Modifier.weight(1f)
                            )

                            // Swap button
                            IconButton(
                                onClick = {
                                    val temp = fromUnit
                                    fromUnit = toUnit
                                    toUnit = temp
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(KeyFunctionBg, RoundedCornerShape(8.dp))
                                    .testTag("conv_swap_btn")
                            ) {
                                Icon(Icons.Default.SwapHoriz, contentDescription = "Swap", tint = PrimaryCyan)
                            }

                            // To Unit Dropdown
                            UnitDropdownMenu(
                                selectedUnit = toUnit,
                                allUnits = unitsForCat,
                                isArabic = isArabic,
                                onSelect = { toUnit = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Result Big Display Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("conv_result_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isArabic) "النتيجة المحولة:" else "Converted Result:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.8g", convertedResult).trimEnd('0').trimEnd('.') + " " + toUnit.symbol,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                        Text(
                            text = if (isArabic) toUnit.nameAr else toUnit.nameEn,
                            fontSize = 12.sp,
                            color = KeyShiftGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Simultaneous All Units Conversion List
                Text(
                    text = if (isArabic) "تحويل فوري لكافة وحدات ${selectedCategory.nameAr}:" else "All ${selectedCategory.nameEn} Conversions:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(unitsForCat) { unit ->
                        val res = UnitConverterData.convert(numericInput, fromUnit, unit, selectedCategory)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF161E28), RoundedCornerShape(6.dp))
                                .clickable { toUnit = unit }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isArabic) unit.nameAr else unit.nameEn,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = unit.symbol,
                                    fontSize = 10.sp,
                                    color = KeyShiftGold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = String.format("%.6g", res).trimEnd('0').trimEnd('.') + " " + unit.symbol,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = if (unit == toUnit) PrimaryCyan else Color(0xFFECEFF1)
                            )
                        }
                    }
                }
            }

            2 -> {
                // History Tab
                if (historyList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isArabic) "لا يوجد سجل تحويلات حتى الآن" else "No conversion history yet",
                            color = Color.Gray
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "سجل التحويلات الأخيرة:" else "Recent Conversions:",
                            fontSize = 13.sp,
                            color = Color.LightGray
                        )
                        TextButton(onClick = { historyList.clear() }) {
                            Text(if (isArabic) "مسح" else "Clear", color = Color.Red, fontSize = 12.sp)
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(historyList) { item ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2631)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (isArabic) item.category.nameAr else item.category.nameEn,
                                            fontSize = 11.sp,
                                            color = KeyShiftGold
                                        )
                                        Text(
                                            text = "${item.fromValue} ${item.fromUnit.symbol} = ${String.format("%.6g", item.toValue)} ${item.toUnit.symbol}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitDropdownMenu(
    selectedUnit: ConversionUnit,
    allUnits: List<ConversionUnit>,
    isArabic: Boolean,
    onSelect: (ConversionUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (isArabic) "${selectedUnit.symbol} (${selectedUnit.nameAr})" else "${selectedUnit.symbol} (${selectedUnit.nameEn})",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF1E2631))
        ) {
            allUnits.forEach { unit ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (isArabic) "${unit.symbol} - ${unit.nameAr}" else "${unit.symbol} - ${unit.nameEn}",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    },
                    onClick = {
                        onSelect(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}
