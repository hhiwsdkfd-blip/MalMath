package com.example.ui.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class KeyConfig(
    val main: String,
    val shift: String? = null,
    val alpha: String? = null,
    val tag: String,
    val bgColor: Color = KeySecondarySlateBg,
    val textColor: Color = KeyNumberText,
    val fontSize: TextUnit = 13.5.sp,
    val isItalic: Boolean = false,
    val isPrimaryAction: Boolean = false
)

@Composable
fun KeypadButton(
    config: KeyConfig,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 40.dp
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .padding(horizontal = 1.5.dp, vertical = 1.5.dp)
            .testTag(config.tag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Shift & Alpha labels above key
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(11.dp)
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = config.shift ?: "",
                color = Color(0xFFF6C445),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
            Text(
                text = config.alpha ?: "",
                color = Color(0xFFEA5B4B),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
        }

        // Main Beveled Surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(6.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = Color.White.copy(alpha = 0.25f))
                ) { onClick() },
            shape = RoundedCornerShape(6.dp),
            color = config.bgColor,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF384958))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.main,
                    color = config.textColor,
                    fontSize = config.fontSize,
                    fontWeight = if (config.isPrimaryAction) FontWeight.Black else FontWeight.Bold,
                    fontStyle = if (config.isItalic) FontStyle.Italic else FontStyle.Normal,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
fun CalculatorKeypad(
    isSecondMode: Boolean,
    onToggleSecondMode: () -> Unit,
    isShiftActive: Boolean,
    isAlphaActive: Boolean,
    onKeyPress: (String) -> Unit,
    onShiftClick: () -> Unit,
    onAlphaClick: () -> Unit,
    onMenuClick: () -> Unit,
    onConstClick: () -> Unit,
    onConvClick: () -> Unit,
    onHelpClick: () -> Unit,
    onCopyClick: () -> Unit,
    onPasteClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onCursorLeft: () -> Unit,
    onCursorRight: () -> Unit,
    onCursorUp: () -> Unit,
    onCursorDown: () -> Unit,
    onDelete: () -> Unit,
    onAllClear: () -> Unit,
    onEquals: () -> Unit,
    onSdToggle: () -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF141920))
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .testTag("calculator_keypad")
    ) {
        if (!isSecondMode) {
            // ==========================================
            // SCREEN 2: 1st MODE (Primary Casio Keypad)
            // ==========================================

            // Row 0: SHIFT, ALPHA, ←, →, MODE, 2nd
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig(
                        main = "SHIFT",
                        tag = "key_shift",
                        bgColor = if (isShiftActive) Color(0xFFFFB300) else Color(0xFFFBA41A),
                        textColor = Color.Black,
                        fontSize = 12.sp
                    ),
                    onClick = onShiftClick,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig(
                        main = "ALPHA",
                        tag = "key_alpha",
                        bgColor = if (isAlphaActive) Color(0xFFFF5722) else Color(0xFFE24A24),
                        textColor = Color.White,
                        fontSize = 12.sp
                    ),
                    onClick = onAlphaClick,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("←", tag = "key_left", fontSize = 16.sp),
                    onClick = onCursorLeft,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("→", tag = "key_right", fontSize = 16.sp),
                    onClick = onCursorRight,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("MODE", tag = "key_mode", fontSize = 11.5.sp),
                    onClick = onMenuClick,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("2nd", tag = "key_2nd_toggle", isItalic = true, fontSize = 14.sp),
                    onClick = onToggleSecondMode,
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 1: CALC, ∫dx, ▲, ▼, x⁻¹, Logₐx
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("CALC", shift = "SOLVE", alpha = "=", tag = "key_calc"),
                    onClick = { onKeyPress(if (isShiftActive) "solve(" else "CALC") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("∫dx", shift = "d/dx", alpha = ":", tag = "key_integral"),
                    onClick = { onKeyPress(if (isShiftActive) "diff(" else "integrate(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("▲", tag = "key_up", fontSize = 14.sp),
                    onClick = onCursorUp,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("▼", tag = "key_down", fontSize = 14.sp),
                    onClick = onCursorDown,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("x⁻¹", shift = "x!", tag = "key_inv"),
                    onClick = { onKeyPress(if (isShiftActive) "!" else "^(-1)") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Logₐx", shift = "Σ", alpha = "Π", tag = "key_log_ab", fontSize = 12.sp),
                    onClick = { onKeyPress(if (isShiftActive) "sum(" else "log(") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2: ■/■, √■, x², x^□, Log, Ln
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("■/■", shift = "mod", alpha = "÷R", tag = "key_frac", fontSize = 12.sp),
                    onClick = {
                        val token = when {
                            isShiftActive -> "mod("
                            isAlphaActive -> "divr("
                            else -> "/"
                        }
                        onKeyPress(token)
                    },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("√■", shift = "³√□", tag = "key_sqrt"),
                    onClick = { onKeyPress(if (isShiftActive) "cbrt(" else "sqrt(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("x²", shift = "x³", tag = "key_sqr"),
                    onClick = { onKeyPress(if (isShiftActive) "^3" else "^2") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("x^□", shift = "ⁿ√□", tag = "key_pow"),
                    onClick = { onKeyPress("^") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Log", shift = "10^x", tag = "key_log"),
                    onClick = { onKeyPress(if (isShiftActive) "10^" else "log(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Ln", shift = "e^x", tag = "key_ln"),
                    onClick = { onKeyPress(if (isShiftActive) "exp(" else "ln(") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 3: (-), ° ′ ″, hyp, Sin, Cos, Tan
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("(-)", shift = "∠", alpha = "a", tag = "key_neg"),
                    onClick = { onKeyPress(if (isAlphaActive) "a" else "-") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("° ′ ″", shift = "FACT", alpha = "b", tag = "key_dms"),
                    onClick = { onKeyPress(if (isAlphaActive) "b" else "dms(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("hyp", shift = "Abs", alpha = "c", tag = "key_hyp"),
                    onClick = { onKeyPress(if (isShiftActive) "abs(" else if (isAlphaActive) "c" else "sinh(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Sin", shift = "Sin⁻¹", alpha = "d", tag = "key_sin"),
                    onClick = { onKeyPress(if (isShiftActive) "asin(" else if (isAlphaActive) "d" else "sin(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Cos", shift = "Cos⁻¹", alpha = "e", tag = "key_cos"),
                    onClick = { onKeyPress(if (isShiftActive) "acos(" else if (isAlphaActive) "e" else "cos(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Tan", shift = "Tan⁻¹", alpha = "f", tag = "key_tan"),
                    onClick = { onKeyPress(if (isShiftActive) "atan(" else if (isAlphaActive) "f" else "tan(") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 4: RCL, ENG, (, ), S⇔D, M+
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("RCL", shift = "STO", alpha = "CLRv", tag = "key_rcl"),
                    onClick = { onKeyPress(if (isShiftActive) "STO" else if (isAlphaActive) "CLR" else "RCL") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("ENG", shift = "i", alpha = "Cot", tag = "key_eng"),
                    onClick = { onKeyPress(if (isShiftActive) "i" else if (isAlphaActive) "cot(" else "ENG") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("(", shift = "%", alpha = "Cot⁻¹", tag = "key_lparen"),
                    onClick = { onKeyPress(if (isShiftActive) "%" else if (isAlphaActive) "acot(" else "(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig(")", shift = ",", alpha = "x", tag = "key_rparen"),
                    onClick = { onKeyPress(if (isShiftActive) "," else if (isAlphaActive) "x" else ")") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("S⇔D", shift = "a b/c", alpha = "y", tag = "key_sd", textColor = PrimaryCyan),
                    onClick = onSdToggle,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("M+", shift = "M-", alpha = "m", tag = "key_mplus"),
                    onClick = { onKeyPress(if (isShiftActive) "M-" else if (isAlphaActive) "m" else "M+") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Row 5: 7, 8, 9, DEL, AC
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("7", shift = "CONST", tag = "key_7", fontSize = 18.sp),
                    onClick = { if (isShiftActive) onConstClick() else onKeyPress("7") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("8", shift = "CONV", tag = "key_8", fontSize = 18.sp),
                    onClick = { if (isShiftActive) onConvClick() else onKeyPress("8") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("9", shift = "∞", tag = "key_9", fontSize = 18.sp),
                    onClick = { onKeyPress("9") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("DEL", tag = "key_del", bgColor = Color(0xFF1976D2), textColor = Color.White, fontSize = 16.sp, isPrimaryAction = true),
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("AC", tag = "key_ac", bgColor = Color(0xFF1976D2), textColor = Color.White, fontSize = 16.sp, isPrimaryAction = true),
                    onClick = onAllClear,
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 6: 4, 5, 6, ×, ÷
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("4", shift = "MATRIX", tag = "key_4", fontSize = 18.sp),
                    onClick = { onKeyPress("4") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("5", shift = "VECTOR", tag = "key_5", fontSize = 18.sp),
                    onClick = { onKeyPress("5") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("6", shift = "HELP", tag = "key_6", fontSize = 18.sp),
                    onClick = { if (isShiftActive) onHelpClick() else onKeyPress("6") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("×", shift = "nPr", alpha = "GCD", tag = "key_mul", fontSize = 20.sp),
                    onClick = { onKeyPress(if (isShiftActive) "nPr(" else if (isAlphaActive) "gcd(" else "*") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("÷", shift = "nCr", alpha = "LCM", tag = "key_div", fontSize = 20.sp),
                    onClick = { onKeyPress(if (isShiftActive) "nCr(" else if (isAlphaActive) "lcm(" else "/") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 7: 1, 2, 3, +, −
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("1", shift = "STAT", tag = "key_1", fontSize = 18.sp),
                    onClick = { onKeyPress("1") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("2", shift = "CMPLX", tag = "key_2", fontSize = 18.sp),
                    onClick = { onKeyPress("2") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("3", shift = "DISTR", tag = "key_3", fontSize = 18.sp),
                    onClick = { onKeyPress(if (isShiftActive) "distr(" else "3") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("+", shift = "Pol", alpha = "Ceil", tag = "key_plus", fontSize = 20.sp),
                    onClick = { onKeyPress(if (isShiftActive) "pol(" else if (isAlphaActive) "ceil(" else "+") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("−", shift = "Rec", alpha = "Floor", tag = "key_minus", fontSize = 20.sp),
                    onClick = { onKeyPress(if (isShiftActive) "rec(" else if (isAlphaActive) "floor(" else "-") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 8: 0, ., Exp, Ans, =
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("0", shift = "COPY", alpha = "PASTE", tag = "key_0", fontSize = 18.sp),
                    onClick = {
                        if (isShiftActive) onCopyClick()
                        else if (isAlphaActive) onPasteClick()
                        else onKeyPress("0")
                    },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig(".", shift = "Ran#", alpha = "RanInt", tag = "key_dot", fontSize = 20.sp),
                    onClick = { onKeyPress(if (isShiftActive) "Ran#" else if (isAlphaActive) "ranint(" else ".") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Exp", shift = "π", alpha = "e", tag = "key_exp", fontSize = 14.sp),
                    onClick = { onKeyPress(if (isShiftActive) "π" else if (isAlphaActive) "e" else "*10^") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Ans", shift = "PreAns", tag = "key_ans", fontSize = 14.sp),
                    onClick = { onKeyPress(if (isShiftActive) "preans" else "Ans") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("=", shift = "History", tag = "key_equals", fontSize = 22.sp),
                    onClick = { if (isShiftActive) onHistoryClick() else onEquals() },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // ==========================================
            // SCREEN 1: 2nd MODE (Secondary Advanced Keypad)
            // ==========================================

            // Row 0: SHIFT, ALPHA, { }, x, y, 1st
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("SHIFT", tag = "key_shift_2nd", bgColor = if (isShiftActive) Color(0xFFFFB300) else Color(0xFFFBA41A), textColor = Color.Black, fontSize = 12.sp),
                    onClick = onShiftClick,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("ALPHA", tag = "key_alpha_2nd", bgColor = if (isAlphaActive) Color(0xFFFF5722) else Color(0xFFE24A24), textColor = Color.White, fontSize = 12.sp),
                    onClick = onAlphaClick,
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("{ }", tag = "key_braces", fontSize = 13.sp),
                    onClick = { onKeyPress("{") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("x", tag = "key_var_x_2nd", fontSize = 14.sp),
                    onClick = { onKeyPress("x") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("y", tag = "key_var_y_2nd", fontSize = 14.sp),
                    onClick = { onKeyPress("y") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("1st", tag = "key_1st_toggle", isItalic = true, fontSize = 14.sp),
                    onClick = onToggleSecondMode,
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 1: ∫, d/dx, FACTOR, EXPAND, SIMPLY, GRAPH
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("∫", tag = "key_2nd_integral", fontSize = 16.sp),
                    onClick = { onKeyPress("integrate(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("d/dx", shift = "Divisors", tag = "key_2nd_diff", fontSize = 12.sp),
                    onClick = { onKeyPress(if (isShiftActive) "divisors(" else "diff(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("FACTOR", shift = "Prime", tag = "key_factor", fontSize = 10.sp),
                    onClick = { onKeyPress(if (isShiftActive) "prime(" else "factor(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("EXPAND", shift = "Solve", tag = "key_expand", fontSize = 10.sp),
                    onClick = { onKeyPress(if (isShiftActive) "solve(" else "expand(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("SIMPLY", tag = "key_simply", fontSize = 10.sp),
                    onClick = { onKeyPress("simplify(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("GRAPH", tag = "key_graph", fontSize = 10.sp),
                    onClick = { onCategoryClick("GRAPH") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2: Conju, Gamma, dot, Cross, Vector Angle, Euclid Distance
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("Conju", shift = "Re", alpha = "Im", tag = "key_conju", fontSize = 10.5.sp),
                    onClick = { onKeyPress("conj(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Gamma", shift = "Arg", alpha = "i", tag = "key_gamma", fontSize = 10.5.sp),
                    onClick = { onKeyPress(if (isShiftActive) "arg(" else "gamma(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("dot", shift = "Projection", tag = "key_dot_vec", fontSize = 11.5.sp),
                    onClick = { onKeyPress("dot(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Cross", shift = "Norm", tag = "key_cross_vec", fontSize = 11.sp),
                    onClick = { onKeyPress(if (isShiftActive) "norm(" else "cross(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Vector\nAngle", shift = "Normalize", tag = "key_vec_angle", fontSize = 9.sp),
                    onClick = { onKeyPress("vangle(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Euclid\nDistance", tag = "key_euclid_dist", fontSize = 8.5.sp),
                    onClick = { onKeyPress("dist(") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 3: v(1×2), v(1×3), [3×1], [3×2], [3×3], [⋮ ⋮]
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("v(1×2)", shift = "[1×1]", tag = "key_v12", fontSize = 10.sp),
                    onClick = { onKeyPress("[0, 0]") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("v(1×3)", shift = "[3×1]", tag = "key_v13", fontSize = 10.sp),
                    onClick = { onKeyPress("[0, 0, 0]") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("[3×1]", shift = "[2×1]", tag = "key_m31", fontSize = 10.sp),
                    onClick = { onKeyPress("[[0], [0], [0]]") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("[3×2]", shift = "[2×2]", tag = "key_m32", fontSize = 10.sp),
                    onClick = { onKeyPress("[[0, 0], [0, 0], [0, 0]]") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("[3×3]", shift = "[2×3]", tag = "key_m33", fontSize = 10.sp),
                    onClick = { onKeyPress("[[1, 0, 0], [0, 1, 0], [0, 0, 1]]") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("[⋮ ⋮]", shift = "[4×4]", tag = "key_m44", fontSize = 11.sp),
                    onClick = { onKeyPress("[[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 4: Tr, Det, [ ]ᵀ, [ ]⁻¹, Eigenvalues, Eigenvectors
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("Tr", shift = "Transpose", tag = "key_tr", fontSize = 12.sp),
                    onClick = { onKeyPress(if (isShiftActive) "transpose(" else "tr(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Det", tag = "key_det_2nd", fontSize = 12.sp),
                    onClick = { onKeyPress("det(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("[ ]ᵀ", tag = "key_transpose_sym", fontSize = 12.sp),
                    onClick = { onKeyPress("ᵀ") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("[ ]⁻¹", shift = "Rank", tag = "key_inv_mat", fontSize = 12.sp),
                    onClick = { onKeyPress(if (isShiftActive) "rank(" else "⁻¹") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Eigen\nvalues", shift = "RowReduce", tag = "key_eigenval", fontSize = 9.sp),
                    onClick = { onKeyPress(if (isShiftActive) "rref(" else "eigenval(") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Eigen\nvectors", shift = "Diagonal", tag = "key_eigenvec", fontSize = 8.5.sp),
                    onClick = { onKeyPress(if (isShiftActive) "diag(" else "eigenvec(") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 5: r, s, t, u, v, z
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("r", tag = "key_var_r", fontSize = 15.sp),
                    onClick = { onKeyPress("r") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("s", tag = "key_var_s", fontSize = 15.sp),
                    onClick = { onKeyPress("s") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("t", shift = "(", tag = "key_var_t", fontSize = 15.sp),
                    onClick = { onKeyPress(if (isShiftActive) "(" else "t") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("u", shift = ")", tag = "key_var_u", fontSize = 15.sp),
                    onClick = { onKeyPress(if (isShiftActive) ")" else "u") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("v", shift = "DEL", tag = "key_var_v", fontSize = 15.sp),
                    onClick = { if (isShiftActive) onDelete() else onKeyPress("v") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("z", shift = "AC", tag = "key_var_z", fontSize = 15.sp),
                    onClick = { if (isShiftActive) onAllClear() else onKeyPress("z") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 6: and, or, xor, Not, True, False
            Row(modifier = Modifier.fillMaxWidth()) {
                KeypadButton(
                    config = KeyConfig("and", shift = "<", alpha = "≤", tag = "key_and", fontSize = 13.sp),
                    onClick = {
                        val token = when {
                            isShiftActive -> "<"
                            isAlphaActive -> "<="
                            else -> " and "
                        }
                        onKeyPress(token)
                    },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("or", shift = ">", alpha = "≥", tag = "key_or", fontSize = 13.sp),
                    onClick = {
                        val token = when {
                            isShiftActive -> ">"
                            isAlphaActive -> ">="
                            else -> " or "
                        }
                        onKeyPress(token)
                    },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("xor", shift = "=", alpha = "≠", tag = "key_xor", fontSize = 13.sp),
                    onClick = {
                        val token = when {
                            isShiftActive -> "=="
                            isAlphaActive -> "!="
                            else -> " xor "
                        }
                        onKeyPress(token)
                    },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("Not", shift = "Max", alpha = "Min", tag = "key_not_bool", fontSize = 12.sp),
                    onClick = {
                        val token = when {
                            isShiftActive -> "max("
                            isAlphaActive -> "min("
                            else -> "not "
                        }
                        onKeyPress(token)
                    },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("True", shift = "Positive", tag = "key_true", fontSize = 12.sp),
                    onClick = { onKeyPress("True") },
                    modifier = Modifier.weight(1f)
                )
                KeypadButton(
                    config = KeyConfig("False", shift = "Negative", tag = "key_false", fontSize = 12.sp),
                    onClick = { onKeyPress("False") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Row 7 (Category Buttons Row 1): Combinatorics, Common, Algebra, Statistic
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryGridButton(label = "Combinat\norics", tag = "cat_btn_combinatorics", onClick = { onCategoryClick("Combinatorics") }, modifier = Modifier.weight(1f))
                CategoryGridButton(label = "Common", tag = "cat_btn_common", onClick = { onCategoryClick("Common") }, modifier = Modifier.weight(1f))
                CategoryGridButton(label = "Algebra", tag = "cat_btn_algebra", onClick = { onCategoryClick("Algebra") }, modifier = Modifier.weight(1f))
                CategoryGridButton(label = "Statistic", tag = "cat_btn_statistic", onClick = { onCategoryClick("Statistic") }, modifier = Modifier.weight(1f))
            }

            // Row 8 (Category Buttons Row 2): Linear Algebra, Others, Number, Boolean
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryGridButton(label = "Linear\nAlgebra", tag = "cat_btn_linalg", onClick = { onCategoryClick("Linear Algebra") }, modifier = Modifier.weight(1f))
                CategoryGridButton(label = "Others", tag = "cat_btn_others", onClick = { onCategoryClick("Others") }, modifier = Modifier.weight(1f))
                CategoryGridButton(label = "Number", tag = "cat_btn_number", onClick = { onCategoryClick("Number") }, modifier = Modifier.weight(1f))
                CategoryGridButton(label = "Boolean", tag = "cat_btn_boolean", onClick = { onCategoryClick("Boolean") }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CategoryGridButton(
    label: String,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(1.5.dp)
            .height(38.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .testTag(tag),
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFEAA539),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6942D))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }
}
