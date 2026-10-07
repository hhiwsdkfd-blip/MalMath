package com.example.model

import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * Exact rational number representation for Natural Textbook Display.
 */
data class Fraction(val num: Long, val den: Long = 1L) : Comparable<Fraction> {
    val numerator: Long
    val denominator: Long

    init {
        require(den != 0L) { "Denominator cannot be zero" }
        val g = gcd(abs(num), abs(den))
        val sign = if (den < 0) -1 else 1
        numerator = (num / g) * sign
        denominator = abs(den) / g
    }

    operator fun plus(other: Fraction): Fraction =
        Fraction(numerator * other.denominator + other.numerator * denominator, denominator * other.denominator)

    operator fun minus(other: Fraction): Fraction =
        Fraction(numerator * other.denominator - other.numerator * denominator, denominator * other.denominator)

    operator fun times(other: Fraction): Fraction =
        Fraction(numerator * other.numerator, denominator * other.denominator)

    operator fun div(other: Fraction): Fraction {
        require(other.numerator != 0L) { "Division by zero" }
        return Fraction(numerator * other.denominator, denominator * other.numerator)
    }

    operator fun unaryMinus(): Fraction = Fraction(-numerator, denominator)

    fun toDouble(): Double = numerator.toDouble() / denominator.toDouble()

    fun isInteger(): Boolean = denominator == 1L

    /**
     * Converts to mixed fraction format: whole + rem/den (e.g. 7/2 -> "3 1/2")
     */
    fun toMixedString(): String {
        if (denominator == 1L) return numerator.toString()
        val whole = numerator / denominator
        val rem = abs(numerator % denominator)
        return if (whole == 0L) {
            "$numerator/$denominator"
        } else {
            "$whole \u200B$rem/$denominator"
        }
    }

    override fun toString(): String {
        return if (denominator == 1L) "$numerator" else "$numerator/$denominator"
    }

    override fun compareTo(other: Fraction): Int {
        val diff = numerator * other.denominator - other.numerator * denominator
        return when {
            diff < 0 -> -1
            diff > 0 -> 1
            else -> 0
        }
    }

    companion object {
        val ZERO = Fraction(0, 1)
        val ONE = Fraction(1, 1)

        fun gcd(a: Long, b: Long): Long {
            var x = a
            var y = b
            while (y != 0L) {
                val temp = y
                y = x % y
                x = temp
            }
            return if (x == 0L) 1L else x
        }

        fun lcm(a: Long, b: Long): Long = if (a == 0L || b == 0L) 0L else abs(a * b) / gcd(a, b)

        /**
         * Approximates a double to a fraction using continued fractions.
         */
        fun fromDouble(value: Double, maxDenominator: Long = 10000L): Fraction? {
            if (value.isNaN() || value.isInfinite()) return null
            if (abs(value - value.roundToLong()) < 1e-10) {
                return Fraction(value.roundToLong(), 1L)
            }

            var x = value
            val sign = if (x < 0) -1 else 1
            x = abs(x)

            var h0 = 0L; var h1 = 1L
            var k0 = 1L; var k1 = 0L

            var a = x.toLong()
            var h2 = a * h1 + h0
            var k2 = a * k1 + k0

            var iterations = 0
            while (abs(x - a.toDouble()) > 1e-11 && k2 <= maxDenominator && iterations < 15) {
                iterations++
                x = 1.0 / (x - a.toDouble())
                a = x.toLong()
                h0 = h1; h1 = h2
                k0 = k1; k1 = k2
                h2 = a * h1 + h0
                k2 = a * k1 + k0
            }

            return if (k2 <= maxDenominator && k2 > 0) {
                val approx = h2.toDouble() / k2.toDouble()
                if (abs(approx - abs(value)) < 1e-6) {
                    Fraction(sign * h2, k2)
                } else null
            } else if (k1 <= maxDenominator && k1 > 0) {
                val approx = h1.toDouble() / k1.toDouble()
                if (abs(approx - abs(value)) < 1e-6) {
                    Fraction(sign * h1, k1)
                } else null
            } else null
        }
    }
}
