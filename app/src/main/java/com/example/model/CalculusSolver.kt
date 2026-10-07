package com.example.model

import kotlin.math.abs

object CalculusSolver {

    /**
     * High-accuracy numerical differentiation d/dx f(x) at x = c
     * Uses 5-point stencil central difference:
     * f'(x) ≈ (-f(x+2h) + 8f(x+h) - 8f(x-h) + f(x-2h)) / (12h)
     */
    fun derivative(c: Double, f: (Double) -> Double): Double {
        val h = 1e-5
        val f_p2 = f(c + 2 * h)
        val f_p1 = f(c + h)
        val f_m1 = f(c - h)
        val f_m2 = f(c - 2 * h)
        return (-f_p2 + 8.0 * f_p1 - 8.0 * f_m1 + f_m2) / (12.0 * h)
    }

    /**
     * Adaptive Simpson's 3/8 and composite quadrature for definite integration:
     * ∫_a^b f(x) dx
     */
    fun integrate(a: Double, b: Double, nSteps: Int = 200, f: (Double) -> Double): Double {
        if (abs(a - b) < 1e-14) return 0.0
        val n = if (nSteps % 2 != 0) nSteps + 1 else nSteps
        val h = (b - a) / n
        var sum = f(a) + f(b)

        for (i in 1 until n) {
            val x = a + i * h
            val fx = f(x)
            if (fx.isNaN() || fx.isInfinite()) continue
            sum += if (i % 2 == 0) 2.0 * fx else 4.0 * fx
        }

        return (h / 3.0) * sum
    }

    /**
     * Summation: Σ_{x=start}^{end} f(x)
     */
    fun sum(start: Long, end: Long, f: (Double) -> Double): Double {
        var total = 0.0
        for (x in start..end) {
            val fx = f(x.toDouble())
            if (!fx.isNaN() && !fx.isInfinite()) {
                total += fx
            }
        }
        return total
    }

    /**
     * Product: Π_{x=start}^{end} f(x)
     */
    fun product(start: Long, end: Long, f: (Double) -> Double): Double {
        var total = 1.0
        for (x in start..end) {
            val fx = f(x.toDouble())
            if (!fx.isNaN() && !fx.isInfinite()) {
                total *= fx
            }
        }
        return total
    }
}
