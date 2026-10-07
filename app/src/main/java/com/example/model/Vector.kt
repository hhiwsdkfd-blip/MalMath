package com.example.model

import java.util.Locale
import kotlin.math.*

/**
 * Mathematical vector (dim 2, 3, or 4)
 */
data class MathVector(val components: DoubleArray) {
    val dim: Int get() = components.size

    operator fun get(index: Int): Double = components[index]

    operator fun plus(other: MathVector): MathVector {
        require(dim == other.dim) { "Dimension mismatch for vector addition" }
        return MathVector(DoubleArray(dim) { components[it] + other.components[it] })
    }

    operator fun minus(other: MathVector): MathVector {
        require(dim == other.dim) { "Dimension mismatch for vector subtraction" }
        return MathVector(DoubleArray(dim) { components[it] - other.components[it] })
    }

    operator fun times(scalar: Double): MathVector =
        MathVector(DoubleArray(dim) { components[it] * scalar })

    /** Dot product (u • v) */
    infix fun dot(other: MathVector): Double {
        require(dim == other.dim) { "Dimension mismatch for dot product" }
        var sum = 0.0
        for (i in 0 until dim) sum += components[i] * other.components[i]
        return sum
    }

    /** Cross product (u × v) - valid for 3D */
    infix fun cross(other: MathVector): MathVector {
        require(dim == 3 && other.dim == 3) { "Cross product is defined for 3D vectors" }
        val u = components
        val v = other.components
        return MathVector(doubleArrayOf(
            u[1] * v[2] - u[2] * v[1],
            u[2] * v[0] - u[0] * v[2],
            u[0] * v[1] - u[1] * v[0]
        ))
    }

    /** Magnitude / Euclidean norm |v| */
    val norm: Double get() {
        var sum = 0.0
        for (c in components) sum += c * c
        return sqrt(sum)
    }

    /** Unit vector */
    val unitVector: MathVector get() {
        val n = norm
        require(n > 1e-12) { "Zero vector has no unit vector" }
        return this * (1.0 / n)
    }

    /** Angle between this vector and other in degrees */
    fun angleDeg(other: MathVector): Double {
        val d = dot(other)
        val denom = norm * other.norm
        require(denom > 1e-12) { "Cannot find angle with zero vector" }
        val cosVal = (d / denom).coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(cosVal))
    }

    /** Angle between this vector and other in radians */
    fun angleRad(other: MathVector): Double {
        val d = dot(other)
        val denom = norm * other.norm
        require(denom > 1e-12) { "Cannot find angle with zero vector" }
        val cosVal = (d / denom).coerceIn(-1.0, 1.0)
        return acos(cosVal)
    }

    fun format(precision: Int = 3): String {
        val sb = StringBuilder("[ ")
        for (i in components.indices) {
            val v = components[i]
            val str = if (v == v.toLong().toDouble()) v.toLong().toString()
            else String.format(Locale.US, "%.${precision}f", v).trimEnd('0').trimEnd('.')
            sb.append(str).append(if (i < dim - 1) ", " else "")
        }
        sb.append(" ]")
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MathVector) return false
        if (dim != other.dim) return false
        for (i in 0 until dim) {
            if (abs(components[i] - other.components[i]) > 1e-9) return false
        }
        return true
    }

    override fun hashCode(): Int = components.contentHashCode()
}
