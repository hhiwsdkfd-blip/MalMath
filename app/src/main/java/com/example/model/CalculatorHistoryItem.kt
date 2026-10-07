package com.example.model

data class CalculatorHistoryItem(
    val id: Long = System.currentTimeMillis(),
    val expression: String,
    val result: String,
    val exactResult: String? = null,
    val mode: CalculatorMode = CalculatorMode.CALCULATE,
    val timestamp: Long = System.currentTimeMillis()
)
