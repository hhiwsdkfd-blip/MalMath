package com.example.model

import java.util.Locale
import kotlin.math.*

enum class AngleUnit(val label: String, val indicator: String) {
    DEG("Degrees", "D"),
    RAD("Radians", "R"),
    GRAD("Gradians", "G")
}

data class EvalResult(
    val decimalValue: Double,
    val exactFraction: Fraction? = null,
    val complexValue: ComplexNumber? = null,
    val isComplex: Boolean = false,
    val isExact: Boolean = false,
    val formatted: String,
    val rawResult: String
)

class MathEvaluator(
    var angleUnit: AngleUnit = AngleUnit.DEG,
    val variables: MutableMap<String, Double> = mutableMapOf(
        "A" to 0.0, "B" to 0.0, "C" to 0.0, "D" to 0.0, "E" to 0.0, "F" to 0.0,
        "X" to 0.0, "Y" to 0.0, "M" to 0.0, "ANS" to 0.0
    )
) {
    var lastAnswer: Double = 0.0

    /**
     * Evaluates a mathematical string expression.
     */
    fun evaluate(rawExpr: String, xValue: Double? = null): EvalResult {
        var expr = rawExpr.trim()
        if (expr.isEmpty()) {
            return EvalResult(0.0, Fraction.ZERO, ComplexNumber.ZERO, false, true, "0", "0")
        }

        // Check if complex calculation is involved (standalone i or digit followed by i)
        val hasComplexI = Regex("""(?<![a-zA-Z])i(?![a-zA-Z])|\d+i\b""").containsMatchIn(expr)
        if (hasComplexI) {
            return tryEvaluateComplex(expr)
        }

        // Replace common visual tokens
        expr = preprocessExpression(expr, xValue)

        val parser = ExpressionParser(expr, angleUnit, variables, this)
        val value = parser.parse()

        lastAnswer = value
        variables["ANS"] = value

        // Try exact fraction matching
        val frac = Fraction.fromDouble(value)
        val formatted = formatResult(value, frac)

        return EvalResult(
            decimalValue = value,
            exactFraction = frac,
            complexValue = ComplexNumber(value, 0.0),
            isComplex = false,
            isExact = frac != null,
            formatted = formatted,
            rawResult = value.toString()
        )
    }

    private fun preprocessExpression(expr: String, xValue: Double?): String {
        var s = expr
            .replace("kotlin.math.", "")
            .replace("Math.", "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", Math.PI.toString())
            .replace("PI", Math.PI.toString())

        if (xValue != null) {
            s = s.replace(Regex("\\bx\\b", RegexOption.IGNORE_CASE), "($xValue)")
        }

        // Ran# -> random double between 0 and 1
        s = s.replace("Ran#", Math.random().toString())

        return s
    }

    private fun tryEvaluateComplex(expr: String): EvalResult {
        // Simple complex evaluator for expressions of type (a + bi) op (c + di)
        return try {
            val res = parseComplex(expr)
            EvalResult(
                decimalValue = res.re,
                exactFraction = null,
                complexValue = res,
                isComplex = true,
                isExact = false,
                formatted = res.toRectangularString(),
                rawResult = res.toString()
            )
        } catch (_: Exception) {
            // Fallback to real evaluator
            val realExpr = expr.replace("i", "0")
            evaluate(realExpr)
        }
    }

    private fun parseComplex(input: String): ComplexNumber {
        var s = input.replace("×", "*").replace("÷", "/").replace("−", "-").replace(" ", "")
        // Handle basic addition/multiplication of complex numbers
        if (s == "i") return ComplexNumber.I
        if (s == "-i") return -ComplexNumber.I

        // Check for (a+bi)+(c+di) etc.
        val complexPattern = Regex("""(-?\d*\.?\d*)\s*([+-])\s*(\d*\.?\d*)i""")
        val match = complexPattern.matchEntire(s)
        if (match != null) {
            val (reStr, sign, imStr) = match.destructured
            val re = if (reStr.isEmpty() || reStr == "-") (if (reStr == "-") -1.0 else 0.0) else reStr.toDouble()
            val imVal = if (imStr.isEmpty()) 1.0 else imStr.toDouble()
            val im = if (sign == "-") -imVal else imVal
            return ComplexNumber(re, im)
        }

        if (s.endsWith("i")) {
            val num = s.dropLast(1)
            val v = when (num) {
                "" -> 1.0
                "-" -> -1.0
                else -> num.toDouble()
            }
            return ComplexNumber(0.0, v)
        }

        return ComplexNumber(s.toDouble(), 0.0)
    }

    private fun formatResult(value: Double, frac: Fraction?): String {
        if (value.isNaN()) return "NaN"
        if (value.isInfinite()) return if (value > 0) "∞" else "-∞"

        // Exact zero
        if (abs(value) < 1e-12) return "0"

        // Integer check
        if (abs(value - round(value)) < 1e-11 && abs(value) < 1e12) {
            return round(value).toLong().toString()
        }

        // Clean small decimals
        return String.format(Locale.US, "%.10g", value).replace(Regex("""\.?0+(e|$)""")) { m ->
            if (m.value.startsWith("e")) m.value else ""
        }
    }

    fun toAngleUnits(rad: Double): Double = when (angleUnit) {
        AngleUnit.RAD -> rad
        AngleUnit.DEG -> Math.toDegrees(rad)
        AngleUnit.GRAD -> rad * 200.0 / Math.PI
    }

    fun fromAngleUnits(angle: Double): Double = when (angleUnit) {
        AngleUnit.RAD -> angle
        AngleUnit.DEG -> Math.toRadians(angle)
        AngleUnit.GRAD -> angle * Math.PI / 200.0
    }
}

/**
 * Recursive descent parser for scientific expressions.
 */
class ExpressionParser(
    private val text: String,
    private val angleUnit: AngleUnit,
    private val variables: Map<String, Double>,
    private val evaluator: MathEvaluator
) {
    private var pos = 0
    private var ch = if (text.isNotEmpty()) text[0] else '\u0000'

    private fun nextChar() {
        pos++
        ch = if (pos < text.length) text[pos] else '\u0000'
    }

    private fun eat(charToEat: Char): Boolean {
        while (ch == ' ') nextChar()
        if (ch == charToEat) {
            nextChar()
            return true
        }
        return false
    }

    fun parse(): Double {
        val x = parseExpression()
        if (pos < text.length) throw IllegalArgumentException("Unexpected character '${ch}' at $pos in $text")
        return x
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor | term `%` factor
    // factor = `+` factor | `-` factor | power
    // power = primary `^` factor | primary
    // primary = ( expression ) | number | function | variable

    private fun parseExpression(): Double {
        var x = parseTerm()
        while (true) {
            when {
                eat('+') -> x += parseTerm()
                eat('-') -> x -= parseTerm()
                else -> return x
            }
        }
    }

    private fun parseTerm(): Double {
        var x = parseFactor()
        while (true) {
            when {
                eat('*') -> x *= parseFactor()
                eat('/') -> {
                    val d = parseFactor()
                    if (abs(d) < 1e-15) throw ArithmeticException("Division by zero")
                    x /= d
                }
                eat('%') -> x %= parseFactor()
                else -> return x
            }
        }
    }

    private fun parseFactor(): Double {
        if (eat('+')) return +parseFactor()
        if (eat('-')) return -parseFactor()
        return parsePower()
    }

    private fun parsePower(): Double {
        var x = parsePrimary()
        // Check for factorial postfix !
        while (eat('!')) {
            x = factorial(x)
        }
        if (eat('^')) {
            val exponent = parseFactor()
            x = x.pow(exponent)
        }
        return x
    }

    private fun parsePrimary(): Double {
        while (ch == ' ') nextChar()
        val startPos = pos

        if (eat('(')) {
            val x = parseExpression()
            if (!eat(')')) throw IllegalArgumentException("Missing closing parenthesis")
            return x
        }

        // Numbers
        if ((ch in '0'..'9') || ch == '.') {
            while ((ch in '0'..'9') || ch == '.') nextChar()
            // Optional scientific notation 1e5
            if (ch == 'e' || ch == 'E') {
                nextChar()
                if (ch == '+' || ch == '-') nextChar()
                while (ch in '0'..'9') nextChar()
            }
            val numStr = text.substring(startPos, pos)
            return numStr.toDouble()
        }

        // Functions or identifiers
        if ((ch in 'a'..'z') || (ch in 'A'..'Z') || ch == '∫' || ch == 'Σ' || ch == '√') {
            if (ch == '√') {
                nextChar()
                val arg = parsePrimary()
                if (arg < 0) throw ArithmeticException("Negative square root")
                return sqrt(arg)
            }

            while ((ch in 'a'..'z') || (ch in 'A'..'Z') || (ch in '0'..'9') || ch == '_' || ch == '#') nextChar()
            val name = text.substring(startPos, pos)

            // Check if variable
            if (variables.containsKey(name.uppercase())) {
                return variables[name.uppercase()] ?: 0.0
            }

            // Function with argument: name(arg) or name(arg1, arg2)
            if (eat('(')) {
                return parseFunction(name)
            } else {
                // Constants like pi, e
                return when (name.lowercase()) {
                    "pi" -> Math.PI
                    "e" -> Math.E
                    "ans" -> variables["ANS"] ?: 0.0
                    "preans" -> variables["PREANS"] ?: 0.0
                    else -> throw IllegalArgumentException("Unknown function or identifier '$name'")
                }
            }
        }

        throw IllegalArgumentException("Unexpected character '$ch' at index $pos")
    }

    private fun parseFunction(name: String): Double {
        val args = mutableListOf<Double>()
        // Parse comma separated arguments until ')'
        if (!eat(')')) {
            while (true) {
                // If it's an expression function like integrate(f(x), a, b), handle specially
                if (name.lowercase() in listOf("int", "integrate", "diff", "sum")) {
                    return handleCalculusFunction(name)
                }
                args.add(parseExpression())
                if (eat(',')) continue
                if (eat(')')) break
                throw IllegalArgumentException("Expected ',' or ')' in function $name")
            }
        }

        val a = args.getOrElse(0) { 0.0 }
        val b = args.getOrElse(1) { 0.0 }
        val c = args.getOrElse(2) { 0.0 }

        return when (name.lowercase()) {
            "sin" -> sin(evaluator.fromAngleUnits(a))
            "cos" -> cos(evaluator.fromAngleUnits(a))
            "tan" -> {
                val rad = evaluator.fromAngleUnits(a)
                if (abs(cos(rad)) < 1e-12) throw ArithmeticException("Math ERROR: tan undefined")
                tan(rad)
            }
            "cot" -> {
                val rad = evaluator.fromAngleUnits(a)
                if (abs(sin(rad)) < 1e-12) throw ArithmeticException("Math ERROR: cot undefined")
                1.0 / tan(rad)
            }
            "asin", "arcsin" -> evaluator.toAngleUnits(asin(a))
            "acos", "arccos" -> evaluator.toAngleUnits(acos(a))
            "atan", "arctan" -> evaluator.toAngleUnits(atan(a))
            "acot", "arccot" -> evaluator.toAngleUnits(atan(1.0 / a))
            "sinh" -> sinh(a)
            "cosh" -> cosh(a)
            "tanh" -> tanh(a)
            "asinh" -> ln(a + sqrt(a * a + 1.0))
            "acosh" -> {
                if (a < 1.0) throw ArithmeticException("acosh domain error: a >= 1")
                ln(a + sqrt(a * a - 1.0))
            }
            "atanh" -> {
                if (abs(a) >= 1.0) throw ArithmeticException("atanh domain error: |a| < 1")
                0.5 * ln((1.0 + a) / (1.0 - a))
            }
            "ln" -> {
                if (a <= 0) throw ArithmeticException("ln of non-positive number")
                ln(a)
            }
            "log" -> {
                if (a <= 0) throw ArithmeticException("log of non-positive number")
                if (args.size > 1) ln(a) / ln(b) else log10(a)
            }
            "log10" -> log10(a)
            "sqrt" -> {
                if (a < 0) throw ArithmeticException("sqrt of negative number")
                sqrt(a)
            }
            "cbrt" -> Math.cbrt(a)
            "abs" -> abs(a)
            "floor" -> floor(a)
            "ceil" -> ceil(a)
            "round" -> round(a)
            "exp" -> exp(a)
            "pow" -> a.pow(b)
            "hypot" -> hypot(a, b)
            "min" -> min(a, b)
            "max" -> max(a, b)
            "mod" -> a % b
            "divr" -> floor(a / b)
            "pol" -> hypot(a, b) // magnitude
            "rec" -> a * cos(evaluator.fromAngleUnits(b)) // x coordinate
            "dms" -> a + (b / 60.0) + (c / 3600.0) // convert degrees, minutes, seconds to decimal
            "distr", "normpdf" -> (1.0 / sqrt(2.0 * Math.PI)) * exp(-0.5 * a * a)
            "normcdf" -> 0.5 * (1.0 + erf(a / sqrt(2.0)))
            "npr" -> StatisticsEngine.permutations(a.toLong(), b.toLong()).toDouble()
            "ncr" -> StatisticsEngine.combinations(a.toLong(), b.toLong()).toDouble()
            "gcd" -> Fraction.gcd(a.toLong(), b.toLong()).toDouble()
            "lcm" -> Fraction.lcm(a.toLong(), b.toLong()).toDouble()
            "ranint" -> {
                val min = a.toInt()
                val max = b.toInt()
                (min..max).random().toDouble()
            }
            else -> throw IllegalArgumentException("Unknown function '$name'")
        }
    }

    private fun handleCalculusFunction(name: String): Double {
        // Collect string up to closing paren
        var depth = 1
        val argSb = StringBuilder()
        while (pos < text.length && depth > 0) {
            val c = text[pos]
            if (c == '(') depth++
            else if (c == ')') {
                depth--
                if (depth == 0) {
                    pos++
                    ch = if (pos < text.length) text[pos] else '\u0000'
                    break
                }
            }
            argSb.append(c)
            pos++
            ch = if (pos < text.length) text[pos] else '\u0000'
        }

        val parts = splitTopLevelCommas(argSb.toString())
        if (parts.size < 2) throw IllegalArgumentException("Calculus function $name requires arguments")

        val fnExpr = parts[0]
        val lowerStr = parts[1]
        val upperStr = parts.getOrNull(2)

        return when (name.lowercase()) {
            "int", "integrate" -> {
                val a = evaluator.evaluate(lowerStr).decimalValue
                val b = evaluator.evaluate(upperStr ?: "0").decimalValue
                CalculusSolver.integrate(a, b) { x ->
                    evaluator.evaluate(fnExpr, x).decimalValue
                }
            }
            "diff" -> {
                val c = evaluator.evaluate(lowerStr).decimalValue
                CalculusSolver.derivative(c) { x ->
                    evaluator.evaluate(fnExpr, x).decimalValue
                }
            }
            "sum" -> {
                val a = evaluator.evaluate(lowerStr).decimalValue.toLong()
                val b = evaluator.evaluate(upperStr ?: "0").decimalValue.toLong()
                CalculusSolver.sum(a, b) { x ->
                    evaluator.evaluate(fnExpr, x).decimalValue
                }
            }
            else -> 0.0
        }
    }

    private fun splitTopLevelCommas(s: String): List<String> {
        val result = mutableListOf<String>()
        var depth = 0
        val current = StringBuilder()
        for (c in s) {
            if (c == '(') depth++
            else if (c == ')') depth--

            if (c == ',' && depth == 0) {
                result.add(current.toString().trim())
                current.clear()
            } else {
                current.append(c)
            }
        }
        if (current.isNotEmpty()) result.add(current.toString().trim())
        return result
    }

    private fun factorial(n: Double): Double {
        val longVal = n.toLong()
        if (abs(n - longVal) > 1e-10 || longVal < 0) {
            // Gamma approximation
            return gamma(n + 1.0)
        }
        if (longVal > 170) return Double.POSITIVE_INFINITY
        var res = 1.0
        for (i in 2..longVal) res *= i
        return res
    }

    private fun gamma(z: Double): Double {
        val g = 7
        val p = doubleArrayOf(
            0.99999999999980993, 676.5203681218851, -1259.1392167224028,
            771.32342877765313, -176.61502916214059, 12.507343278686905,
            -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7
        )
        var x = z - 1
        var a = p[0]
        val t = x + g + 0.5
        for (i in 1 until p.size) {
            a += p[i] / (x + i)
        }
        return sqrt(2 * Math.PI) * t.pow(x + 0.5) * exp(-t) * a
    }

    private fun erf(x: Double): Double {
        // Abramowitz and Stegun approximation
        val a1 = 0.254829592
        val a2 = -0.284496736
        val a3 = 1.421413741
        val a4 = -1.453152027
        val a5 = 1.061405429
        val p = 0.3275911

        val sign = if (x < 0) -1 else 1
        val absX = abs(x)
        val t = 1.0 / (1.0 + p * absX)
        val y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * exp(-absX * absX)
        return sign * y
    }
}
