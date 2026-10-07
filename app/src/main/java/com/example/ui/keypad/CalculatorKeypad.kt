package com.example.ui.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CalculatorKeypad(
    isShiftActive: Boolean,
    isAlphaActive: Boolean,
    isSecondActive: Boolean,
    isHypActive: Boolean,
    onKeyPress: (String) -> Unit,
    onShiftClick: () -> Unit,
    onAlphaClick: () -> Unit,
    onSecondClick: () -> Unit,
    onHypClick: () -> Unit,
    onMenuClick: () -> Unit,
    onOptionClick: () -> Unit,
    onConstClick: () -> Unit,
    onConvClick: () -> Unit,
    onHelpClick: () -> Unit,
    onCopyClick: () -> Unit,
    onPasteClick: () -> Unit,
    onCursorLeft: () -> Unit,
    onCursorRight: () -> Unit,
    onHistoryUp: () -> Unit,
    onHistoryDown: () -> Unit,
    onDelete: () -> Unit,
    onAllClear: () -> Unit,
    onEquals: () -> Unit,
    onSdToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag("calculator_keypad")
    ) {
        // --- 1. TOP CONTROL BAR (Shift, Alpha, D-Pad, Menu, Optn) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Shift and Alpha
            Column(
                modifier = Modifier.width(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SmallPillButton(
                    label = "SHIFT",
                    textColor = if (isShiftActive) Color.Black else KeyShiftGold,
                    bgColor = if (isShiftActive) KeyShiftGold else KeyShiftGoldBg,
                    tag = "key_shift",
                    onClick = onShiftClick
                )
                SmallPillButton(
                    label = "ALPHA",
                    textColor = if (isAlphaActive) Color.White else KeyAlphaPink,
                    bgColor = if (isAlphaActive) KeyAlphaPink else KeyAlphaPinkBg,
                    tag = "key_alpha",
                    onClick = onAlphaClick
                )
            }

            // Center: Circular Metallic D-Pad Replay Wheel
            ReplayDpadWheel(
                onUp = onHistoryUp,
                onDown = onHistoryDown,
                onLeft = onCursorLeft,
                onRight = onCursorRight
            )

            // Right: Menu/Setup and OPTN
            Column(
                modifier = Modifier.width(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SmallPillButton(
                    label = "MENU",
                    subLabel = "MODE",
                    textColor = Color.White,
                    bgColor = KeyFunctionBg,
                    tag = "key_menu",
                    onClick = onMenuClick
                )
                SmallPillButton(
                    label = "OPTN",
                    subLabel = "SETUP",
                    textColor = Color.White,
                    bgColor = KeyFunctionBg,
                    tag = "key_optn",
                    onClick = onOptionClick
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // --- 1.5 SECONDARY SCIENTIFIC EXTENSION BAR (2nd, hyp, mod, ÷R, Abs, GCD, LCM, PreAns, COPY, PASTE, HELP) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SmallPillButton(
                label = "2nd",
                textColor = if (isSecondActive) Color.Black else PrimaryCyan,
                bgColor = if (isSecondActive) PrimaryCyan else Color(0xFF1E2631),
                tag = "key_2nd",
                onClick = onSecondClick,
                width = 54.dp
            )
            SmallPillButton(
                label = "hyp",
                textColor = if (isHypActive) Color.Black else KeyShiftGold,
                bgColor = if (isHypActive) KeyShiftGold else Color(0xFF1E2631),
                tag = "key_hyp",
                onClick = onHypClick,
                width = 54.dp
            )
            SmallPillButton(
                label = "mod",
                textColor = Color.White,
                bgColor = KeyFunctionBg,
                tag = "key_mod",
                onClick = { onKeyPress("mod(") },
                width = 54.dp
            )
            SmallPillButton(
                label = "÷R",
                textColor = Color.White,
                bgColor = KeyFunctionBg,
                tag = "key_div_r",
                onClick = { onKeyPress("divr(") },
                width = 54.dp
            )
            SmallPillButton(
                label = "Abs",
                textColor = Color.White,
                bgColor = KeyFunctionBg,
                tag = "key_abs",
                onClick = { onKeyPress("abs(") },
                width = 54.dp
            )
            SmallPillButton(
                label = "GCD",
                textColor = Color.White,
                bgColor = KeyFunctionBg,
                tag = "key_gcd",
                onClick = { onKeyPress("gcd(") },
                width = 54.dp
            )
            SmallPillButton(
                label = "LCM",
                textColor = Color.White,
                bgColor = KeyFunctionBg,
                tag = "key_lcm",
                onClick = { onKeyPress("lcm(") },
                width = 54.dp
            )
            SmallPillButton(
                label = "PreAns",
                textColor = KeyShiftGold,
                bgColor = KeyFunctionBg,
                tag = "key_preans",
                onClick = { onKeyPress("preans") },
                width = 62.dp
            )
            SmallPillButton(
                label = "COPY",
                textColor = Color.LightGray,
                bgColor = KeyFunctionBg,
                tag = "key_copy",
                onClick = onCopyClick,
                width = 54.dp
            )
            SmallPillButton(
                label = "PASTE",
                textColor = Color.LightGray,
                bgColor = KeyFunctionBg,
                tag = "key_paste",
                onClick = onPasteClick,
                width = 54.dp
            )
            SmallPillButton(
                label = "CONST",
                textColor = KeyShiftGold,
                bgColor = KeyFunctionBg,
                tag = "key_const_top",
                onClick = onConstClick,
                width = 60.dp
            )
            SmallPillButton(
                label = "CONV",
                textColor = PrimaryCyan,
                bgColor = KeyFunctionBg,
                tag = "key_conv_top",
                onClick = onConvClick,
                width = 58.dp
            )
            SmallPillButton(
                label = "HELP",
                textColor = Color(0xFF81C784),
                bgColor = KeyFunctionBg,
                tag = "key_help",
                onClick = onHelpClick,
                width = 54.dp
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // --- 2. SCIENTIFIC FUNCTION ROW 1 ---
        // CALC, ∫dx, x⁻¹, log_a(b), Fraction, √( )
        Row(modifier = Modifier.fillMaxWidth()) {
            KeypadButton(
                config = KeyConfig("CALC", shift = "SOLVE", alpha = "=", tag = "key_calc", bgColor = KeyFunctionBg),
                onClick = { onKeyPress("CALC") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("∫dx", shift = "d/dx", alpha = ":", tag = "key_integral", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "diff(" else "integrate(") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("x⁻¹", shift = "x!", tag = "key_inv", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "!" else "^(-1)") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("logₐb", shift = "Σ", tag = "key_log_ab", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "sum(" else "log(") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("分数", shift = "a b/c", tag = "key_frac", bgColor = KeyFunctionBg, fontSize = 13.sp),
                onClick = { onKeyPress("/") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("√□", shift = "∛□", tag = "key_sqrt", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "cbrt(" else "sqrt(") },
                modifier = Modifier.weight(1f)
            )
        }

        // --- 3. SCIENTIFIC FUNCTION ROW 2 ---
        // x², x^□, log, ln, (-), ° ' "
        Row(modifier = Modifier.fillMaxWidth()) {
            KeypadButton(
                config = KeyConfig("x²", shift = "x³", tag = "key_sqr", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "^3" else "^2") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("x^□", shift = "ⁿ√", tag = "key_pow", bgColor = KeyFunctionBg),
                onClick = { onKeyPress("^") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("log", shift = "10^x", tag = "key_log", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "10^" else "log(") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("ln", shift = "e^x", tag = "key_ln", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "exp(" else "ln(") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("(-)", shift = "A", alpha = "A", tag = "key_neg", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isAlphaActive) "A" else "-") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("° ' \"", shift = "B", alpha = "B", tag = "key_dms", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isAlphaActive) "B" else "dms(") },
                modifier = Modifier.weight(1f)
            )
        }

        // --- 4. SCIENTIFIC FUNCTION ROW 3 ---
        // x, sin, cos, tan, STO, ENG
        Row(modifier = Modifier.fillMaxWidth()) {
            KeypadButton(
                config = KeyConfig("x", shift = "C", alpha = "x", tag = "key_var_x", bgColor = KeyFunctionBg, textColor = KeyAlphaPink),
                onClick = { onKeyPress("x") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig(
                    main = if (isHypActive) "sinh" else "sin",
                    shift = if (isHypActive) "asinh" else "sin⁻¹",
                    alpha = "D",
                    tag = "key_sin",
                    bgColor = KeyFunctionBg
                ),
                onClick = {
                    val token = when {
                        isHypActive && isShiftActive -> "asinh("
                        isHypActive -> "sinh("
                        isShiftActive -> "asin("
                        isAlphaActive -> "D"
                        else -> "sin("
                    }
                    onKeyPress(token)
                },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig(
                    main = if (isHypActive) "cosh" else "cos",
                    shift = if (isHypActive) "acosh" else "cos⁻¹",
                    alpha = "E",
                    tag = "key_cos",
                    bgColor = KeyFunctionBg
                ),
                onClick = {
                    val token = when {
                        isHypActive && isShiftActive -> "acosh("
                        isHypActive -> "cosh("
                        isShiftActive -> "acos("
                        isAlphaActive -> "E"
                        else -> "cos("
                    }
                    onKeyPress(token)
                },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig(
                    main = if (isHypActive) "tanh" else "tan",
                    shift = if (isHypActive) "atanh" else "tan⁻¹",
                    alpha = "F",
                    tag = "key_tan",
                    bgColor = KeyFunctionBg
                ),
                onClick = {
                    val token = when {
                        isHypActive && isShiftActive -> "atanh("
                        isHypActive -> "tanh("
                        isShiftActive -> "atan("
                        isAlphaActive -> "F"
                        else -> "tan("
                    }
                    onKeyPress(token)
                },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig(
                    main = if (isSecondActive) "cot" else "STO",
                    shift = if (isSecondActive) "cot⁻¹" else "RCL",
                    tag = "key_sto",
                    bgColor = KeyFunctionBg
                ),
                onClick = {
                    if (isSecondActive) {
                        onKeyPress(if (isShiftActive) "acot(" else "cot(")
                    } else {
                        onKeyPress(if (isShiftActive) "RCL" else "STO")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("ENG", shift = "←", tag = "key_eng", bgColor = KeyFunctionBg),
                onClick = { onKeyPress("ENG") },
                modifier = Modifier.weight(1f)
            )
        }

        // --- 5. SCIENTIFIC FUNCTION ROW 4 ---
        // (, ), S⇔D, M+
        Row(modifier = Modifier.fillMaxWidth()) {
            KeypadButton(
                config = KeyConfig("(", shift = "%", alpha = "Y", tag = "key_lparen", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "%" else if (isAlphaActive) "Y" else "(") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig(")", shift = ",", alpha = "Z", tag = "key_rparen", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "," else if (isAlphaActive) "Z" else ")") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("S⇔D", shift = "a b/c", tag = "key_sd", bgColor = KeyFunctionBg, textColor = PrimaryCyan),
                onClick = onSdToggle,
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("M+", shift = "M-", alpha = "M", tag = "key_mplus", bgColor = KeyFunctionBg),
                onClick = { onKeyPress(if (isShiftActive) "M-" else if (isAlphaActive) "M" else "M+") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // --- 6. NUMERIC KEYPAD & STANDARD OPERATIONS ---
        // Row 1: 7, 8, 9, DEL, AC
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
                config = KeyConfig("9", shift = "CLR", tag = "key_9", fontSize = 18.sp),
                onClick = { if (isShiftActive) onKeyPress("CLR") else onKeyPress("9") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("DEL", shift = "INS", tag = "key_del", bgColor = KeyActionDelBg, textColor = KeyActionDelText),
                onClick = onDelete,
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("AC", shift = "OFF", tag = "key_ac", bgColor = KeyActionClearBg, textColor = KeyActionClearText, isPrimaryAction = true),
                onClick = onAllClear,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: 4, 5, 6, ×, ÷
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
                config = KeyConfig("6", shift = "VERIF", tag = "key_6", fontSize = 18.sp),
                onClick = { onKeyPress("6") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("×", shift = "nPr", tag = "key_mul", bgColor = KeyFunctionBg, fontSize = 20.sp),
                onClick = { onKeyPress(if (isShiftActive) "nPr(" else "*") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("÷", shift = "nCr", tag = "key_div", bgColor = KeyFunctionBg, fontSize = 20.sp),
                onClick = { onKeyPress(if (isShiftActive) "nCr(" else "/") },
                modifier = Modifier.weight(1f)
            )
        }

        // Row 3: 1, 2, 3, +, −
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
                config = KeyConfig("3", shift = "BASE", tag = "key_3", fontSize = 18.sp),
                onClick = { onKeyPress("3") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("+", shift = "Pol", tag = "key_plus", bgColor = KeyFunctionBg, fontSize = 20.sp),
                onClick = { onKeyPress(if (isShiftActive) "pol(" else "+") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("−", shift = "Rec", tag = "key_minus", bgColor = KeyFunctionBg, fontSize = 20.sp),
                onClick = { onKeyPress(if (isShiftActive) "rec(" else "-") },
                modifier = Modifier.weight(1f)
            )
        }

        // Row 4: 0, •, ×10ˣ, Ans, =
        Row(modifier = Modifier.fillMaxWidth()) {
            KeypadButton(
                config = KeyConfig("0", shift = "Rnd", tag = "key_0", fontSize = 18.sp),
                onClick = { onKeyPress("0") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("•", shift = "Ran#", alpha = "RanInt", tag = "key_dot", fontSize = 20.sp),
                onClick = { onKeyPress(if (isShiftActive) "Ran#" else if (isAlphaActive) "ranint(" else ".") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("×10ˣ", shift = "π", alpha = "e", tag = "key_exp", bgColor = KeyFunctionBg, fontSize = 13.sp),
                onClick = { onKeyPress(if (isShiftActive) "π" else if (isAlphaActive) "e" else "*10^") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("Ans", shift = "PreAns", tag = "key_ans", bgColor = KeyFunctionBg, fontSize = 14.sp),
                onClick = { onKeyPress(if (isShiftActive) "preans" else "Ans") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                config = KeyConfig("=", tag = "key_equals", bgColor = KeyEqualsBg, textColor = KeyEqualsText, fontSize = 22.sp, isPrimaryAction = true),
                onClick = onEquals,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SmallPillButton(
    label: String,
    subLabel: String? = null,
    textColor: Color,
    bgColor: Color,
    tag: String,
    onClick: () -> Unit,
    width: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp.Unspecified
) {
    Surface(
        modifier = (if (width != androidx.compose.ui.unit.Dp.Unspecified) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(tag),
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF37474F))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = textColor
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    fontSize = 6.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = KeyShiftGold
                )
            }
        }
    }
}

@Composable
private fun ReplayDpadWheel(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF37474F), Color(0xFF1E2631))
                )
            )
            .border(2.dp, Color(0xFF546E7A), CircleShape)
            .testTag("dpad_wheel"),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color(0xFF101419))
                .border(1.dp, Color(0xFF263238), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "REPLAY",
                fontSize = 5.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF90A4AE),
                fontFamily = FontFamily.Monospace
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clickable { onUp() }
                .padding(top = 4.dp)
                .testTag("dpad_up")
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Up",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable { onDown() }
                .padding(bottom = 4.dp)
                .testTag("dpad_down")
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Down",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onLeft() }
                .padding(start = 4.dp)
                .testTag("dpad_left")
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Left",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onRight() }
                .padding(end = 4.dp)
                .testTag("dpad_right")
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Right",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
