package com.example

import androidx.lifecycle.ViewModel
import com.example.model.*
import com.example.ui.components.DecimalDisplayFormat
import com.example.ui.display.DisplayTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

data class CalculatorUiState(
    val expression: String = "",
    val cursorPosition: Int = 0,
    val result: String = "",
    val exactResult: String? = null,
    val isExactMode: Boolean = true,
    val errorMessage: String? = null,
    val isShiftActive: Boolean = false,
    val isAlphaActive: Boolean = false,
    val isSecondActive: Boolean = false,
    val isHypActive: Boolean = false,
    val isMemoryActive: Boolean = false,
    val fontScale: Float = 1.0f,
    val angleUnit: AngleUnit = AngleUnit.DEG,
    val decimalFormat: DecimalDisplayFormat = DecimalDisplayFormat.DECI,
    val currentMode: CalculatorMode = CalculatorMode.CALCULATE,
    val isNaturalDisplay: Boolean = true,
    val displayTheme: DisplayTheme = DisplayTheme.CASIO_LCD,
    val solarActive: Boolean = true,
    val batteryPercent: Int = 98,
    val isArabic: Boolean = true,
    val hapticEnabled: Boolean = true,
    val history: List<CalculatorHistoryItem> = emptyList(),
    val historyIndex: Int = -1,
    val qrPayload: String? = null,
    val showModeMenu: Boolean = false,
    val showSettings: Boolean = false,
    val showHistory: Boolean = false,
    val showMemoryDialog: Boolean = false,
    val showConstantsDialog: Boolean = false,
    val showHelpDialog: Boolean = false,
    val showCameraDialog: Boolean = false,
    val clipboardText: String = "",
    val preAns: Double = 0.0,
    val matrices: Map<String, Matrix> = mapOf(
        "MatA" to Matrix(2, 2, arrayOf(doubleArrayOf(2.0, 1.0), doubleArrayOf(1.0, 3.0))),
        "MatB" to Matrix(2, 2, arrayOf(doubleArrayOf(1.0, 0.0), doubleArrayOf(0.0, 1.0))),
        "MatC" to Matrix(3, 3, arrayOf(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(0.0, 1.0, 4.0), doubleArrayOf(5.0, 6.0, 0.0)))
    ),
    val vectors: Map<String, MathVector> = mapOf(
        "VctA" to MathVector(doubleArrayOf(1.0, 2.0, 3.0)),
        "VctB" to MathVector(doubleArrayOf(4.0, 5.0, 6.0)),
        "VctC" to MathVector(doubleArrayOf(1.0, 0.0, -1.0))
    )
)

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    val evaluator = MathEvaluator()

    fun onKeyPress(token: String) {
        val state = _uiState.value

        when (token) {
            "CALC" -> evaluate()
            "STO" -> {
                setMemoryDialogOpen(true)
            }
            "RCL" -> {
                setMemoryDialogOpen(true)
            }
            "CLR" -> {
                clearAllMemory()
            }
            "M+" -> {
                val addVal = state.result.toDoubleOrNull() ?: (evaluator.evaluate(state.expression.ifEmpty { "0" }).decimalValue)
                val curM = evaluator.variables["M"] ?: 0.0
                evaluator.variables["M"] = curM + addVal
                _uiState.update { it.copy(isMemoryActive = true) }
            }
            "M-" -> {
                val subVal = state.result.toDoubleOrNull() ?: (evaluator.evaluate(state.expression.ifEmpty { "0" }).decimalValue)
                val curM = evaluator.variables["M"] ?: 0.0
                evaluator.variables["M"] = curM - subVal
                _uiState.update { it.copy(isMemoryActive = true) }
            }
            "ENG" -> {
                if (state.result.isNotEmpty()) {
                    val v = state.result.toDoubleOrNull()
                    if (v != null && v != 0.0) {
                        val exp = (Math.floor(Math.log10(Math.abs(v)) / 3.0) * 3).toInt()
                        val man = v / Math.pow(10.0, exp.toDouble())
                        _uiState.update { it.copy(result = "$man × 10^$exp") }
                    }
                }
            }
            else -> {
                insertText(token)
            }
        }

        // Reset Shift, Alpha, Second, Hyp after action if active
        if (state.isShiftActive || state.isAlphaActive || state.isSecondActive || state.isHypActive) {
            _uiState.update { it.copy(isShiftActive = false, isAlphaActive = false, isSecondActive = false, isHypActive = false) }
        }
    }

    fun toggleShift() {
        _uiState.update { it.copy(isShiftActive = !it.isShiftActive, isAlphaActive = false) }
    }

    fun toggleAlpha() {
        _uiState.update { it.copy(isAlphaActive = !it.isAlphaActive, isShiftActive = false) }
    }

    fun toggleSecond() {
        _uiState.update { it.copy(isSecondActive = !it.isSecondActive) }
    }

    fun toggleHyp() {
        _uiState.update { it.copy(isHypActive = !it.isHypActive) }
    }

    fun zoomIn() {
        _uiState.update { it.copy(fontScale = (it.fontScale + 0.1f).coerceAtMost(1.5f)) }
    }

    fun zoomOut() {
        _uiState.update { it.copy(fontScale = (it.fontScale - 0.1f).coerceAtLeast(0.7f)) }
    }

    fun resetCurrentInput() {
        _uiState.update { it.copy(expression = "", cursorPosition = 0, errorMessage = null) }
    }

    fun cycleAngleUnit() {
        val nextUnit = when (_uiState.value.angleUnit) {
            AngleUnit.DEG -> AngleUnit.RAD
            AngleUnit.RAD -> AngleUnit.GRAD
            AngleUnit.GRAD -> AngleUnit.DEG
        }
        setAngleUnit(nextUnit)
    }

    fun cycleDecimalFormat() {
        val nextFmt = when (_uiState.value.decimalFormat) {
            DecimalDisplayFormat.DECI -> DecimalDisplayFormat.NORM
            DecimalDisplayFormat.NORM -> DecimalDisplayFormat.FIX
            DecimalDisplayFormat.FIX -> DecimalDisplayFormat.SCI
            DecimalDisplayFormat.SCI -> DecimalDisplayFormat.DECI
        }
        _uiState.update { it.copy(decimalFormat = nextFmt) }
        if (_uiState.value.result.isNotEmpty()) {
            formatCurrentResult(nextFmt)
        }
    }

    private fun formatCurrentResult(format: DecimalDisplayFormat) {
        val num = _uiState.value.result.toDoubleOrNull() ?: return
        val formatted = when (format) {
            DecimalDisplayFormat.DECI -> String.format(Locale.US, "%.6g", num).trimEnd('0').trimEnd('.')
            DecimalDisplayFormat.NORM -> String.format(Locale.US, "%.10g", num)
            DecimalDisplayFormat.FIX -> String.format(Locale.US, "%.2f", num)
            DecimalDisplayFormat.SCI -> String.format(Locale.US, "%.4e", num)
        }
        _uiState.update { it.copy(result = formatted) }
    }

    fun insertText(text: String) {
        _uiState.update { state ->
            val expr = state.expression
            val pos = state.cursorPosition.coerceIn(0, expr.length)
            val newExpr = expr.substring(0, pos) + text + expr.substring(pos)
            state.copy(
                expression = newExpr,
                cursorPosition = pos + text.length,
                errorMessage = null
            )
        }
    }

    fun deleteChar() {
        _uiState.update { state ->
            val expr = state.expression
            val pos = state.cursorPosition
            if (pos > 0 && expr.isNotEmpty()) {
                val newExpr = expr.substring(0, pos - 1) + expr.substring(pos)
                state.copy(
                    expression = newExpr,
                    cursorPosition = pos - 1,
                    errorMessage = null
                )
            } else {
                state
            }
        }
    }

    fun allClear() {
        _uiState.update {
            it.copy(
                expression = "",
                cursorPosition = 0,
                result = "",
                exactResult = null,
                errorMessage = null,
                isShiftActive = false,
                isAlphaActive = false,
                isSecondActive = false,
                isHypActive = false,
                historyIndex = -1
            )
        }
    }

    fun evaluate() {
        val state = _uiState.value
        val expr = state.expression.trim()
        if (expr.isEmpty()) return

        try {
            evaluator.angleUnit = state.angleUnit
            val currentAns = evaluator.lastAnswer

            val evalRes = evaluator.evaluate(expr)

            // Update PreAns with previous Ans
            evaluator.variables["PREANS"] = currentAns

            var decimalStr = evalRes.formatted
            // Format by active decimal format
            evalRes.decimalValue.let { valD ->
                if (!valD.isNaN() && !valD.isInfinite()) {
                    decimalStr = when (state.decimalFormat) {
                        DecimalDisplayFormat.DECI -> String.format(Locale.US, "%.8g", valD).trimEnd('0').trimEnd('.')
                        DecimalDisplayFormat.NORM -> String.format(Locale.US, "%.10g", valD)
                        DecimalDisplayFormat.FIX -> String.format(Locale.US, "%.2f", valD)
                        DecimalDisplayFormat.SCI -> String.format(Locale.US, "%.4e", valD)
                    }
                }
            }

            val exactStr = if (evalRes.exactFraction != null && !evalRes.exactFraction.isInteger()) {
                evalRes.exactFraction.toString()
            } else if (evalRes.isComplex && evalRes.complexValue != null) {
                evalRes.complexValue.toRectangularString()
            } else {
                decimalStr
            }

            val historyItem = CalculatorHistoryItem(
                expression = expr,
                result = decimalStr,
                exactResult = exactStr,
                mode = state.currentMode
            )

            val updatedHistory = listOf(historyItem) + state.history

            _uiState.update {
                it.copy(
                    result = if (it.isExactMode && exactStr != decimalStr) exactStr else decimalStr,
                    exactResult = exactStr,
                    preAns = currentAns,
                    errorMessage = null,
                    history = updatedHistory,
                    historyIndex = -1
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    result = "",
                    exactResult = null,
                    errorMessage = "Syntax ERROR: ${e.message?.take(30) ?: "Check expression"}"
                )
            }
        }
    }

    fun toggleSd() {
        _uiState.update { state ->
            val exact = state.exactResult
            if (exact != null && exact.contains("/")) {
                val newExactMode = !state.isExactMode
                val frac = Fraction.fromDouble(state.result.toDoubleOrNull() ?: 0.0)
                val newResult = if (newExactMode) {
                    exact
                } else {
                    frac?.toDouble()?.toString() ?: state.result
                }
                state.copy(isExactMode = newExactMode, result = newResult)
            } else {
                state
            }
        }
    }

    fun moveCursorLeft() {
        _uiState.update { state ->
            val pos = (state.cursorPosition - 1).coerceAtLeast(0)
            state.copy(cursorPosition = pos)
        }
    }

    fun moveCursorRight() {
        _uiState.update { state ->
            val pos = (state.cursorPosition + 1).coerceAtMost(state.expression.length)
            state.copy(cursorPosition = pos)
        }
    }

    fun historyUp() {
        val state = _uiState.value
        if (state.history.isEmpty()) return
        val nextIdx = (state.historyIndex + 1).coerceAtMost(state.history.size - 1)
        val item = state.history[nextIdx]
        _uiState.update {
            it.copy(
                expression = item.expression,
                cursorPosition = item.expression.length,
                result = item.result,
                exactResult = item.exactResult,
                historyIndex = nextIdx
            )
        }
    }

    fun historyDown() {
        val state = _uiState.value
        if (state.historyIndex <= 0) {
            _uiState.update { it.copy(historyIndex = -1, expression = "", cursorPosition = 0, result = "") }
        } else {
            val nextIdx = state.historyIndex - 1
            val item = state.history[nextIdx]
            _uiState.update {
                it.copy(
                    expression = item.expression,
                    cursorPosition = item.expression.length,
                    result = item.result,
                    exactResult = item.exactResult,
                    historyIndex = nextIdx
                )
            }
        }
    }

    fun setMode(mode: CalculatorMode) {
        _uiState.update { it.copy(currentMode = mode, showModeMenu = false) }
    }

    fun setAngleUnit(unit: AngleUnit) {
        evaluator.angleUnit = unit
        _uiState.update { it.copy(angleUnit = unit) }
    }

    fun setNaturalDisplay(enabled: Boolean) {
        _uiState.update { it.copy(isNaturalDisplay = enabled) }
    }

    fun setDisplayTheme(theme: DisplayTheme) {
        _uiState.update { it.copy(displayTheme = theme) }
    }

    fun setLanguage(isArabic: Boolean) {
        _uiState.update { it.copy(isArabic = isArabic) }
    }

    fun setHaptic(enabled: Boolean) {
        _uiState.update { it.copy(hapticEnabled = enabled) }
    }

    fun toggleSolarLight() {
        _uiState.update { it.copy(solarActive = !it.solarActive) }
    }

    fun showQr(payload: String) {
        _uiState.update { it.copy(qrPayload = payload) }
    }

    fun dismissQr() {
        _uiState.update { it.copy(qrPayload = null) }
    }

    fun setModeMenuOpen(open: Boolean) {
        _uiState.update { it.copy(showModeMenu = open) }
    }

    fun setSettingsOpen(open: Boolean) {
        _uiState.update { it.copy(showSettings = open) }
    }

    fun setHistoryOpen(open: Boolean) {
        _uiState.update { it.copy(showHistory = open) }
    }

    fun setMemoryDialogOpen(open: Boolean) {
        _uiState.update { it.copy(showMemoryDialog = open) }
    }

    fun setConstantsDialogOpen(open: Boolean) {
        _uiState.update { it.copy(showConstantsDialog = open) }
    }

    fun setHelpDialogOpen(open: Boolean) {
        _uiState.update { it.copy(showHelpDialog = open) }
    }

    fun setCameraDialogOpen(open: Boolean) {
        _uiState.update { it.copy(showCameraDialog = open) }
    }

    fun clearHistory() {
        _uiState.update { it.copy(history = emptyList(), historyIndex = -1) }
    }

    fun selectHistoryItem(item: CalculatorHistoryItem) {
        _uiState.update {
            it.copy(
                expression = item.expression,
                cursorPosition = item.expression.length,
                result = item.result,
                exactResult = item.exactResult,
                showHistory = false
            )
        }
    }

    fun updateMatrix(name: String, matrix: Matrix) {
        val newMap = _uiState.value.matrices.toMutableMap()
        newMap[name] = matrix
        _uiState.update { it.copy(matrices = newMap) }
    }

    fun updateVector(name: String, vector: MathVector) {
        val newMap = _uiState.value.vectors.toMutableMap()
        newMap[name] = vector
        _uiState.update { it.copy(vectors = newMap) }
    }

    fun storeVariable(name: String) {
        val curVal = _uiState.value.result.toDoubleOrNull() ?: (evaluator.evaluate(_uiState.value.expression.ifEmpty { "0" }).decimalValue)
        evaluator.variables[name.uppercase()] = curVal
        _uiState.update { it.copy(isMemoryActive = true) }
    }

    fun recallVariable(name: String) {
        insertText(name.uppercase())
    }

    fun clearAllMemory() {
        listOf("A", "B", "C", "D", "E", "F", "X", "Y", "M").forEach {
            evaluator.variables[it] = 0.0
        }
        _uiState.update { it.copy(isMemoryActive = false) }
    }

    fun insertConstant(c: ScientificConstant) {
        insertText(c.value.toString())
    }

    fun copyToClipboard() {
        val textToCopy = _uiState.value.result.ifEmpty { _uiState.value.expression }
        _uiState.update { it.copy(clipboardText = textToCopy) }
    }

    fun pasteFromClipboard() {
        val textToPaste = _uiState.value.clipboardText
        if (textToPaste.isNotEmpty()) {
            insertText(textToPaste)
        }
    }
}
