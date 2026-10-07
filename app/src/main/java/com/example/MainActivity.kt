package com.example

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CalculatorMode
import com.example.ui.components.SolarPanelBar
import com.example.ui.components.TopQuickActionBar
import com.example.ui.dialogs.*
import com.example.ui.display.NaturalMathDisplay
import com.example.ui.keypad.CalculatorKeypad
import com.example.ui.modes.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CalculatorApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val view = LocalView.current
    val context = LocalContext.current

    fun performHaptic() {
        if (state.hapticEnabled) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
    }

    // Handle back button on dedicated modes
    BackHandler(enabled = state.currentMode != CalculatorMode.CALCULATE) {
        viewModel.setMode(CalculatorMode.CALCULATE)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("calculator_main_scaffold"),
        containerColor = CalcChassisDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CalcChassisDark)
        ) {
            // 1. Top Quick Action Bar (☰, √x, ↻, −, +, 📷, DEG, M▼, MATH, DECI)
            TopQuickActionBar(
                angleUnit = state.angleUnit,
                onAngleUnitCycle = {
                    performHaptic()
                    viewModel.cycleAngleUnit()
                },
                isNaturalDisplay = state.isNaturalDisplay,
                onToggleNaturalDisplay = {
                    performHaptic()
                    viewModel.setNaturalDisplay(!state.isNaturalDisplay)
                },
                decimalFormat = state.decimalFormat,
                onDecimalFormatCycle = {
                    performHaptic()
                    viewModel.cycleDecimalFormat()
                },
                isMemoryActive = state.isMemoryActive,
                onOpenMemoryDialog = {
                    performHaptic()
                    viewModel.setMemoryDialogOpen(true)
                },
                onOpenMenu = {
                    performHaptic()
                    viewModel.setModeMenuOpen(true)
                },
                onInsertSqrt = {
                    performHaptic()
                    viewModel.insertText("sqrt(")
                },
                onResetCurrent = {
                    performHaptic()
                    viewModel.resetCurrentInput()
                },
                onZoomIn = {
                    performHaptic()
                    viewModel.zoomIn()
                },
                onZoomOut = {
                    performHaptic()
                    viewModel.zoomOut()
                },
                onOpenCamera = {
                    performHaptic()
                    viewModel.setCameraDialogOpen(true)
                }
            )

            // 2. Solar Cell Dual Power Bar
            SolarPanelBar(
                solarActive = state.solarActive,
                batteryPercent = state.batteryPercent,
                isArabic = state.isArabic,
                onToggleSolarLight = {
                    performHaptic()
                    viewModel.toggleSolarLight()
                }
            )

            // 3. Main Workspace depending on Mode
            if (state.currentMode == CalculatorMode.CALCULATE) {
                // Natural Math Display
                NaturalMathDisplay(
                    expression = state.expression,
                    cursorPosition = state.cursorPosition,
                    result = state.result,
                    exactResult = state.exactResult,
                    isExactMode = state.isExactMode,
                    errorMessage = state.errorMessage,
                    isShiftActive = state.isShiftActive,
                    isAlphaActive = state.isAlphaActive,
                    isMemoryActive = state.isMemoryActive,
                    angleUnit = state.angleUnit,
                    currentMode = state.currentMode,
                    isNaturalDisplay = state.isNaturalDisplay,
                    hasHistoryUp = state.history.isNotEmpty(),
                    hasHistoryDown = state.historyIndex >= 0,
                    displayTheme = state.displayTheme,
                    fontScale = state.fontScale,
                    onSdToggle = {
                        performHaptic()
                        viewModel.toggleSd()
                    },
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Full Scientific Keypad
                CalculatorKeypad(
                    isSecondMode = state.isSecondActive,
                    onToggleSecondMode = {
                        performHaptic()
                        viewModel.toggleSecond()
                    },
                    isShiftActive = state.isShiftActive,
                    isAlphaActive = state.isAlphaActive,
                    onKeyPress = { token ->
                        performHaptic()
                        viewModel.onKeyPress(token)
                    },
                    onShiftClick = {
                        performHaptic()
                        viewModel.toggleShift()
                    },
                    onAlphaClick = {
                        performHaptic()
                        viewModel.toggleAlpha()
                    },
                    onMenuClick = {
                        performHaptic()
                        viewModel.setModeMenuOpen(true)
                    },
                    onConstClick = {
                        performHaptic()
                        viewModel.setConstantsDialogOpen(true)
                    },
                    onConvClick = {
                        performHaptic()
                        viewModel.setMode(CalculatorMode.UNIT_CONVERTER)
                    },
                    onHelpClick = {
                        performHaptic()
                        viewModel.setHelpDialogOpen(true)
                    },
                    onCopyClick = {
                        performHaptic()
                        viewModel.copyToClipboard()
                        Toast.makeText(context, if (state.isArabic) "تم النسخ" else "Copied", Toast.LENGTH_SHORT).show()
                    },
                    onPasteClick = {
                        performHaptic()
                        viewModel.pasteFromClipboard()
                    },
                    onHistoryClick = {
                        performHaptic()
                        viewModel.setHistoryOpen(true)
                    },
                    onCursorLeft = {
                        performHaptic()
                        viewModel.moveCursorLeft()
                    },
                    onCursorRight = {
                        performHaptic()
                        viewModel.moveCursorRight()
                    },
                    onCursorUp = {
                        performHaptic()
                        viewModel.historyUp()
                    },
                    onCursorDown = {
                        performHaptic()
                        viewModel.historyDown()
                    },
                    onDelete = {
                        performHaptic()
                        viewModel.deleteChar()
                    },
                    onAllClear = {
                        performHaptic()
                        viewModel.allClear()
                    },
                    onEquals = {
                        performHaptic()
                        viewModel.evaluate()
                    },
                    onSdToggle = {
                        performHaptic()
                        viewModel.toggleSd()
                    },
                    onCategoryClick = { category ->
                        performHaptic()
                        when (category) {
                            "GRAPH" -> viewModel.setMode(CalculatorMode.TABLE)
                            "Combinatorics" -> viewModel.onKeyPress("nCr(")
                            "Algebra" -> viewModel.setMode(CalculatorMode.EQUATION)
                            "Statistic" -> viewModel.setMode(CalculatorMode.STATISTICS)
                            "Linear Algebra" -> viewModel.setMode(CalculatorMode.MATRIX)
                            "Number" -> viewModel.setMode(CalculatorMode.BASE_N)
                            else -> Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Dedicated Mode Screens
                Box(modifier = Modifier.fillMaxSize()) {
                    when (state.currentMode) {
                        CalculatorMode.UNIT_CONVERTER -> UnitConverterScreen(
                            isArabic = state.isArabic,
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.MATRIX -> MatrixScreen(
                            matrices = state.matrices,
                            onUpdateMatrix = { name, mat -> viewModel.updateMatrix(name, mat) },
                            isArabic = state.isArabic,
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.VECTOR -> VectorScreen(
                            vectors = state.vectors,
                            onUpdateVector = { name, vct -> viewModel.updateVector(name, vct) },
                            isArabic = state.isArabic,
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.EQUATION -> EquationScreen(
                            isArabic = state.isArabic,
                            onShowQr = { viewModel.showQr(it) },
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.STATISTICS -> StatisticsScreen(
                            isArabic = state.isArabic,
                            onShowQr = { viewModel.showQr(it) },
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.TABLE -> TableGraphScreen(
                            evaluator = viewModel.evaluator,
                            isArabic = state.isArabic,
                            onShowQr = { viewModel.showQr(it) },
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.BASE_N -> BaseNScreen(
                            isArabic = state.isArabic,
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.COMPLEX -> ComplexScreen(
                            isArabic = state.isArabic,
                            onBack = { viewModel.setMode(CalculatorMode.CALCULATE) }
                        )
                        CalculatorMode.CALCULATE -> {}
                    }
                }
            }
        }
    }

    // Dialogs
    if (state.showMemoryDialog) {
        MemoryDialog(
            variables = viewModel.evaluator.variables,
            ans = viewModel.evaluator.lastAnswer,
            preAns = state.preAns,
            isArabic = state.isArabic,
            onRecall = { viewModel.recallVariable(it) },
            onStore = { viewModel.storeVariable(it) },
            onClearAll = { viewModel.clearAllMemory() },
            onDismiss = { viewModel.setMemoryDialogOpen(false) }
        )
    }

    if (state.showConstantsDialog) {
        ConstantsDialog(
            isArabic = state.isArabic,
            onSelectConstant = { viewModel.insertConstant(it) },
            onDismiss = { viewModel.setConstantsDialogOpen(false) }
        )
    }

    if (state.showHelpDialog) {
        HelpDialog(
            isArabic = state.isArabic,
            onDismiss = { viewModel.setHelpDialogOpen(false) }
        )
    }

    if (state.showCameraDialog) {
        CameraScanDialog(
            isArabic = state.isArabic,
            onInsertExpression = { expr ->
                viewModel.allClear()
                viewModel.insertText(expr)
                viewModel.evaluate()
            },
            onDismiss = { viewModel.setCameraDialogOpen(false) }
        )
    }

    if (state.qrPayload != null) {
        QrCodeDialog(
            payload = state.qrPayload!!,
            isArabic = state.isArabic,
            onDismiss = { viewModel.dismissQr() }
        )
    }

    if (state.showModeMenu) {
        ModeMenuDialog(
            currentMode = state.currentMode,
            isArabic = state.isArabic,
            onSelectMode = { mode ->
                performHaptic()
                viewModel.setMode(mode)
            },
            onDismiss = { viewModel.setModeMenuOpen(false) }
        )
    }

    if (state.showHistory) {
        HistoryDialog(
            historyList = state.history,
            isArabic = state.isArabic,
            onSelectHistory = { item ->
                performHaptic()
                viewModel.selectHistoryItem(item)
            },
            onClearHistory = {
                performHaptic()
                viewModel.clearHistory()
            },
            onDismiss = { viewModel.setHistoryOpen(false) }
        )
    }

    if (state.showSettings) {
        SettingsDialog(
            angleUnit = state.angleUnit,
            onAngleUnitChange = { viewModel.setAngleUnit(it) },
            isNaturalDisplay = state.isNaturalDisplay,
            onNaturalDisplayToggle = { viewModel.setNaturalDisplay(it) },
            displayTheme = state.displayTheme,
            onThemeChange = { viewModel.setDisplayTheme(it) },
            isArabic = state.isArabic,
            onLanguageToggle = { viewModel.setLanguage(it) },
            hapticEnabled = state.hapticEnabled,
            onHapticToggle = { viewModel.setHaptic(it) },
            onDismiss = { viewModel.setSettingsOpen(false) }
        )
    }
}
