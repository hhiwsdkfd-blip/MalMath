package com.example

import com.example.model.*
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testFractionOperations() {
        val f1 = Fraction(1, 2)
        val f2 = Fraction(1, 3)
        val sum = f1 + f2
        assertEquals(Fraction(5, 6), sum)

        val mult = f1 * f2
        assertEquals(Fraction(1, 6), mult)

        val mixed = Fraction(7, 2).toMixedString()
        assertTrue(mixed.contains("3") && mixed.contains("1/2"))

        val approx = Fraction.fromDouble(0.75)
        assertEquals(Fraction(3, 4), approx)
    }

    @Test
    fun testComplexNumbers() {
        val z1 = ComplexNumber(3.0, 4.0)
        assertEquals(5.0, z1.abs, 1e-9)

        val z2 = ComplexNumber(1.0, -2.0)
        val mult = z1 * z2
        // (3+4i)(1-2i) = 3 - 6i + 4i - 8i^2 = 3 - 2i + 8 = 11 - 2i
        assertEquals(11.0, mult.re, 1e-9)
        assertEquals(-2.0, mult.im, 1e-9)
    }

    @Test
    fun testMatrixDeterminantAndInverse() {
        val mat = Matrix(2, 2, arrayOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)))
        assertEquals(-2.0, mat.determinant(), 1e-9)

        val inv = mat.inverse()
        assertNotNull(inv)
        val identity = mat * inv!!
        assertEquals(1.0, identity[0, 0], 1e-6)
        assertEquals(0.0, identity[0, 1], 1e-6)
        assertEquals(0.0, identity[1, 0], 1e-6)
        assertEquals(1.0, identity[1, 1], 1e-6)
    }

    @Test
    fun testVectorOperations() {
        val u = MathVector(doubleArrayOf(1.0, 0.0, 0.0))
        val v = MathVector(doubleArrayOf(0.0, 1.0, 0.0))
        assertEquals(0.0, u dot v, 1e-9)

        val cross = u cross v
        assertEquals(MathVector(doubleArrayOf(0.0, 0.0, 1.0)), cross)
        assertEquals(90.0, u.angleDeg(v), 1e-9)
    }

    @Test
    fun testEquationSolver() {
        val res = EquationSolver.solveQuadratic(1.0, -5.0, 6.0)
        assertTrue(res is EquationResult.PolynomialSolution)
        val roots = (res as EquationResult.PolynomialSolution).roots.map { it.re }.sorted()
        assertEquals(2.0, roots[0], 1e-6)
        assertEquals(3.0, roots[1], 1e-6)

        val linRes = EquationSolver.solveLinearSystem(
            arrayOf(doubleArrayOf(2.0, 3.0), doubleArrayOf(1.0, -1.0)),
            doubleArrayOf(8.0, -1.0)
        )
        assertTrue(linRes is EquationResult.LinearSolution)
        val vars = (linRes as EquationResult.LinearSolution).variables.toMap()
        assertEquals(1.0, vars["x"]!!, 1e-6)
        assertEquals(2.0, vars["y"]!!, 1e-6)
    }

    @Test
    fun testCalculusAndEvaluator() {
        val eval = MathEvaluator(AngleUnit.DEG)
        val res1 = eval.evaluate("2 + 3 * 4")
        assertEquals(14.0, res1.decimalValue, 1e-9)

        val trig = eval.evaluate("sin(30)")
        assertEquals(0.5, trig.decimalValue, 1e-9)

        val deriv = CalculusSolver.derivative(3.0) { x -> x * x }
        assertEquals(6.0, deriv, 1e-3)

        val integral = CalculusSolver.integrate(0.0, 2.0) { x -> 2.0 * x }
        assertEquals(4.0, integral, 1e-3)
    }

    @Test
    fun testExtendedScientificFunctions() {
        val eval = MathEvaluator(AngleUnit.DEG)
        // cot(45) = 1
        val cotVal = eval.evaluate("cot(45)")
        assertEquals(1.0, cotVal.decimalValue, 1e-6)

        // mod(17, 5) = 2
        val modVal = eval.evaluate("mod(17, 5)")
        assertEquals(2.0, modVal.decimalValue, 1e-9)

        // divr(17, 5) = 3
        val divrVal = eval.evaluate("divr(17, 5)")
        assertEquals(3.0, divrVal.decimalValue, 1e-9)

        // floor & ceil
        val floorVal = eval.evaluate("floor(3.8)")
        assertEquals(3.0, floorVal.decimalValue, 1e-9)
        val ceilVal = eval.evaluate("ceil(3.2)")
        assertEquals(4.0, ceilVal.decimalValue, 1e-9)

        // dms(30, 30, 0) = 30.5
        val dmsVal = eval.evaluate("dms(30, 30, 0)")
        assertEquals(30.5, dmsVal.decimalValue, 1e-9)

        // pol(3, 4) = 5
        val polVal = eval.evaluate("pol(3, 4)")
        assertEquals(5.0, polVal.decimalValue, 1e-9)

        // Kotlin math syntax compatibility
        val kotlinMathVal = eval.evaluate("kotlin.math.sqrt(64) + pow(2, 3)")
        assertEquals(16.0, kotlinMathVal.decimalValue, 1e-9)
    }

    @Test
    fun testUnitConverter() {
        val lenUnits = UnitConverterData.categories[UnitCategory.LENGTH]!!
        val meter = lenUnits.first { it.id == "m" }
        val km = lenUnits.first { it.id == "km" }
        val foot = lenUnits.first { it.id == "ft" }

        // 1 km = 1000 m
        val resM = UnitConverterData.convert(1.0, km, meter, UnitCategory.LENGTH)
        assertEquals(1000.0, resM, 1e-6)

        // 1 m in ft ~ 3.28084 ft
        val resFt = UnitConverterData.convert(1.0, meter, foot, UnitCategory.LENGTH)
        assertEquals(3.28084, resFt, 1e-2)

        // Data binary: 1 KiB = 1024 B
        val dataBinUnits = UnitConverterData.categories[UnitCategory.DATA_BINARY]!!
        val kib = dataBinUnits.first { it.id == "kib" }
        val byteBin = dataBinUnits.first { it.id == "byte_bin" }
        val resBytes = UnitConverterData.convert(1.0, kib, byteBin, UnitCategory.DATA_BINARY)
        assertEquals(1024.0, resBytes, 1e-6)
    }

    @Test
    fun testScientificConstants() {
        val speedOfLight = ScientificConstantsData.constants.first { it.symbol == "c" }
        assertEquals(299792458.0, speedOfLight.value, 1e-6)

        val planck = ScientificConstantsData.constants.first { it.symbol == "h" }
        assertEquals(6.62607015e-34, planck.value, 1e-40)
    }
}
