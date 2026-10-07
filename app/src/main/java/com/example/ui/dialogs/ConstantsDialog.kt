package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import com.example.model.ScientificConstant
import com.example.model.ScientificConstantsData
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

@Composable
fun ConstantsDialog(
    isArabic: Boolean,
    onSelectConstant: (ScientificConstant) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember {
        listOf("All", "Mathematical", "Universal", "Electromagnetic", "Atomic & Nuclear", "Physico-Chemical")
    }

    val filteredList = remember(searchQuery, selectedCategory) {
        ScientificConstantsData.constants.filter { item ->
            val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
            val matchesQuery = searchQuery.isEmpty() ||
                    item.symbol.contains(searchQuery, ignoreCase = true) ||
                    item.nameEn.contains(searchQuery, ignoreCase = true) ||
                    item.nameAr.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(12.dp)
                .testTag("constants_dialog"),
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
                        text = if (isArabic) "الثوابت العلمية والفيزيائية (CONST)" else "SCIENTIFIC CONSTANTS",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = KeyShiftGold,
                        fontFamily = FontFamily.Monospace
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("const_close_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.LightGray) },
                    placeholder = { Text(if (isArabic) "بحث بالاسم أو الرمز..." else "Search constants...", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("const_search_field")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category chips scroll
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ScrollableTabRow(
                        selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                        edgePadding = 0.dp,
                        containerColor = Color.Transparent
                    ) {
                        categories.forEach { cat ->
                            Tab(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                text = { Text(cat, fontSize = 11.sp) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredList) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF141A22)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectConstant(item)
                                    onDismiss()
                                }
                                .testTag("const_item_${item.symbol}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.symbol,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PrimaryCyan,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isArabic) item.nameAr else item.nameEn,
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                    }
                                    Text(
                                        text = "${item.value} ${item.unit}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = KeyShiftGold
                                    )
                                }
                                Button(
                                    onClick = {
                                        onSelectConstant(item)
                                        onDismiss()
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(if (isArabic) "إدراج" else "Insert", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
