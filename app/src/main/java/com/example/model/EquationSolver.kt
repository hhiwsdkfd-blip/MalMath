package com.example.model

import java.util.Locale
import kotlin.math.*

sealed class EquationResult {
    data class LinearSolution(val variables: List<Pair<String, Double>>) : EquationResult()
    data class PolynomialSolution(
        val roots: List<ComplexNumber>,
        val extrema: List<Pair<Double, Double>> = emptyList() // For quadratic vertex (x, y)
    ) : EquationResult()
    data class Error(val message: String) : EquationResult()
    object InfiniteSolutions : EquationResult()
    object NoSolution : EquationResult()
}

object EquationSolver {

    /**
     * Solves linear systems Ax = B for n = 2, 3, 4.
     */
    fun solveLinearSystem(coefficients: Array<DoubleArray>, constants: DoubleArray): EquationResult {
        val n = constants.size
        val augmented = Array(n) { r ->
            DoubleArray(n + 1) { c ->
                if (c < n) coefficients[r][c] else constants[r]
            }
        }

        // Gaussian elimination with partial pivoting
        for (i in 0 until n) {
            var pivot = i
            for (j in i + 1 until n) {
                if (abs(augmented[j][i]) > abs(augmented[pivot][i])) pivot = j
            }
            if (abs(augmented[pivot][i]) < 1e-11) {
                return EquationResult.NoSolution
            }
            if (i != pivot) {
                val tmp = augmented[i]
                augmented[i] = augmented[pivot]
                augmented[pivot] = tmp
            }

            for (j in i + 1 until n) {
                val factor = augmented[j][i] / augmented[i][i]
                for (k in i until n + 1) {
                    augmented[j][k] -= factor * augmented[i][k]
                }
            }
        }

        // Back substitution
        val x = DoubleArray(n)
        for (i in n - 1 downTo 0) {
            var sum = augmented[i][n]
            for (j in i + 1 until n) {
                sum -= augmented[i][j] * x[j]
            }
            if (abs(augmented[i][i]) < 1e-12) {
                return if (abs(sum) < 1e-12) EquationResult.InfiniteSolutions else EquationResult.NoSolution
            }
            x[i] = sum / augmented[i][i]
        }

        val varNames = when (n) {
            2 -> listOf("x", "y")
            3 -> listOf("x", "y", "z")
            else -> listOf("x", "y", "z", "w")
        }
        val result = varNames.mapIndexed { idx, name -> name to x[idx] }
        return EquationResult.LinearSolution(result)
    }

    /**
     * Solves quadratic ax^2 + bx + c = 0
     */
    fun solveQuadratic(a: Double, b: Double, c: Double): EquationResult {
        if (abs(a) < 1e-12) {
            // Linear bx + c = 0
            if (abs(b) < 1e-12) {
                return if (abs(c) < 1e-12) EquationResult.InfiniteSolutions else EquationResult.NoSolution
            }
            return EquationResult.PolynomialSolution(listOf(ComplexNumber(-c / b, 0.0)))
        }

        val delta = b * b - 4 * a * c
        val xVertex = -b / (2 * a)
        val yVertex = a * xVertex * xVertex + b * xVertex + c
        val extrema = listOf(xVertex to yVertex)

        return when {
            delta >= 0.0 -> {
                val r1 = (-b + sqrt(delta)) / (2 * a)
                val r2 = (-b - sqrt(delta)) / (2 * a)
                EquationResult.PolynomialSolution(
                    listOf(ComplexNumber(r1, 0.0), ComplexNumber(r2, 0.0)),
                    extrema
                )
            }
            else -> {
                val real = -b / (2 * a)
                val imag = sqrt(-delta) / (2 * abs(a))
                EquationResult.PolynomialSolution(
                    listOf(ComplexNumber(real, imag), ComplexNumber(real, -imag)),
                    extrema
                )
            }
        }
    }

    /**
     * Solves cubic ax^3 + bx^2 + cx + d = 0 using Cardano's formula / Durand-Kerner
     */
    fun solveCubic(a: Double, b: Double, c: Double, d: Double): EquationResult {
        if (abs(a) < 1e-12) return solveQuadratic(b, c, d)

        // Normalize
        val an = b / a
        val bn = c / a
        val cn = d / a

        val p = bn - an * an / 3.0
        val q = 2.0 * an * an * an / 27.0 - an * bn / 3.0 + cn
        val delta = q * q / 4.0 + p * p * p / 27.0

        val roots = mutableListOf<ComplexNumber>()

        if (abs(delta) < 1e-12) {
            val u = cbrt(-q / 2.0)
            val r1 = 2 * u - an / 3.0
            val r2 = -u - an / 3.0
            roots.add(ComplexNumber(r1, 0.0))
            roots.add(ComplexNumber(r2, 0.0))
            roots.add(ComplexNumber(r2, 0.0))
        } else if (delta > 0.0) {
            val sqrtDelta = sqrt(delta)
            val u = cbrt(-q / 2.0 + sqrtDelta)
            val v = cbrt(-q / 2.0 - sqrtDelta)
            val r1 = u + v - an / 3.0
            val realPart = -(u + v) / 2.0 - an / 3.0
            val imagPart = (u - v) * sqrt(3.0) / 2.0
            roots.add(ComplexNumber(r1, 0.0))
            roots.add(ComplexNumber(realPart, imagPart))
            roots.add(ComplexNumber(realPart, -imagPart))
        } else {
            val r = sqrt(-p * p * p / 27.0)
            val phi = acos((-q / (2.0 * r)).coerceIn(-1.0, 1.0))
            val m = 2.0 * cbrt(r)
            val r1 = m * cos(phi / 3.0) - an / 3.0
            val r2 = m * cos((phi + 2 * Math.PI) / 3.0) - an / 3.0
            val r3 = m * cos((phi + 4 * Math.PI) / 3.0) - an / 3.0
            roots.add(ComplexNumber(r1, 0.0))
            roots.add(ComplexNumber(r2, 0.0))
            roots.add(ComplexNumber(r3, 0.0))
        }

        return EquationResult.PolynomialSolution(roots)
    }

    /**
     * Solves quartic ax^4 + bx^3 + cx^2 + dx + e = 0 using Durand-Kerner method
     */
    fun solveQuartic(a: Double, b: Double, c: Double, d: Double, e: Double): EquationResult {
        if (abs(a) < 1e-12) return solveCubic(b, c, d, e)

        val poly = doubleArrayOf(e / a, d / a, c / a, b / a, 1.0)
        // Initial roots
        var roots = arrayOf(
            ComplexNumber(0.4, 0.9),
            ComplexNumber(-0.4, 0.9),
            ComplexNumber(-0.4, -0.9),
            ComplexNumber(0.4, -0.9)
        )

        for (iter in 0 until 60) {
            val nextRoots = roots.clone()
            for (i in 0 until 4) {
                // evaluate poly at roots[i]
                var pVal = ComplexNumber(poly[4], 0.0)
                for (k in 3 downTo 0) {
                    pVal = pVal * roots[i] + poly[k]
                }
                var denom = ComplexNumber(1.0, 0.0)
                for (j in 0 until 4) {
                    if (j != i) denom = denom * (roots[i] - roots[j])
                }
                if (denom.abs > 1e-14) {
                    nextRoots[i] = roots[i] - (pVal / denom)
                }
            }
            roots = nextRoots
        }

        return EquationResult.PolynomialSolution(roots.toList())
    }

    private fun cbrt(x: Double): Double = if (x < 0) -(-x).pow(1.0 / 3.0) else x.pow(1.0 / 3.0)
}
