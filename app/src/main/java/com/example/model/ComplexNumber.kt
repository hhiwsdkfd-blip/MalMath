package com.example.model

import java.util.Locale
import kotlin.math.*

/**
 * Complex number representation z = a + bi = r * e^(i*θ)
 */
data class ComplexNumber(val re: Double, val im: Double = 0.0) {

    operator fun plus(other: ComplexNumber) = ComplexNumber(re + other.re, im + other.im)
    operator fun plus(other: Double) = ComplexNumber(re + other, im)

    operator fun minus(other: ComplexNumber) = ComplexNumber(re - other.re, im - other.im)
    operator fun minus(other: Double) = ComplexNumber(re - other, im)

    operator fun times(other: ComplexNumber) =
        ComplexNumber(re * other.re - im * other.im, re * other.im + im * other.re)
    operator fun times(other: Double) = ComplexNumber(re * other, im * other)

    operator fun div(other: ComplexNumber): ComplexNumber {
        val denom = other.re * other.re + other.im * other.im
        require(denom != 0.0) { "Complex division by zero" }
        return ComplexNumber(
            (re * other.re + im * other.im) / denom,
            (im * other.re - re * other.im) / denom
        )
    }
    operator fun div(other: Double): ComplexNumber {
        require(other != 0.0) { "Division by zero" }
        return ComplexNumber(re / other, im / other)
    }

    operator fun unaryMinus() = ComplexNumber(-re, -im)

    /** Modulus (absolute value |z| = r) */
    val abs: Double get() = hypot(re, im)

    /** Argument (angle θ in radians) */
    val argRad: Double get() = atan2(im, re)

    /** Argument (angle θ in degrees) */
    val argDeg: Double get() = Math.toDegrees(argRad)

    /** Complex conjugate z* */
    val conjugate: ComplexNumber get() = ComplexNumber(re, -im)

    /** Integer power z^n */
    fun pow(n: Int): ComplexNumber {
        val r = abs.pow(n)
        val theta = argRad * n
        return ComplexNumber(r * cos(theta), r * sin(theta))
    }

    /** Square root */
    fun sqrt(): ComplexNumber {
        val r = kotlin.math.sqrt(abs)
        val theta = argRad / 2.0
        return ComplexNumber(r * cos(theta), r * sin(theta))
    }

    /** Returns rectangular form string: a + bi */
    fun toRectangularString(precision: Int = 4): String {
        val cleanRe = cleanZero(re)
        val cleanIm = cleanZero(im)

        if (abs(cleanIm) < 1e-10) {
            return formatNumber(cleanRe, precision)
        }
        if (abs(cleanRe) < 1e-10) {
            return when {
                abs(cleanIm - 1.0) < 1e-10 -> "i"
                abs(cleanIm + 1.0) < 1e-10 -> "-i"
                else -> "${formatNumber(cleanIm, precision)}i"
            }
        }

        val sign = if (cleanIm < 0) " - " else " + "
        val absIm = abs(cleanIm)
        val imStr = if (abs(absIm - 1.0) < 1e-10) "i" else "${formatNumber(absIm, precision)}i"
        return "${formatNumber(cleanRe, precision)}$sign$imStr"
    }

    /** Returns polar form string: r ∠ θ° or r ∠ θ rad */
    fun toPolarString(inDegrees: Boolean = true, precision: Int = 4): String {
        val r = formatNumber(cleanZero(abs), precision)
        val thetaVal = if (inDegrees) argDeg else argRad
        val theta = formatNumber(cleanZero(thetaVal), precision)
        val unit = if (inDegrees) "°" else " rad"
        return "$r ∠ $theta$unit"
    }

    override fun toString(): String = toRectangularString()

    companion object {
        val ZERO = ComplexNumber(0.0, 0.0)
        val ONE = ComplexNumber(1.0, 0.0)
        val I = ComplexNumber(0.0, 1.0)

        fun fromPolar(r: Double, thetaRad: Double) =
            ComplexNumber(r * cos(thetaRad), r * sin(thetaRad))

        private fun cleanZero(v: Double): Double = if (abs(v) < 1e-12) 0.0 else v

        private fun formatNumber(v: Double, precision: Int): String {
            if (v == v.toLong().toDouble()) return v.toLong().toString()
            return String.format(Locale.US, "%.${precision}f", v).trimEnd('0').trimEnd('.')
        }
    }
}
