package com.example.model

import java.util.Locale
import kotlin.math.abs

/**
 * Real Matrix of dimensions rows x cols (up to 4x4 for scientific calculators).
 */
data class Matrix(
    val rows: Int,
    val cols: Int,
    val data: Array<DoubleArray>
) {
    init {
        require(rows > 0 && cols > 0) { "Matrix dimensions must be positive" }
        require(data.size == rows) { "Data rows count does not match $rows" }
        for (r in data) {
            require(r.size == cols) { "Row column count does not match $cols" }
        }
    }

    operator fun get(r: Int, c: Int): Double = data[r][c]
    fun set(r: Int, c: Int, v: Double) { data[r][c] = v }

    val isSquare: Boolean get() = rows == cols

    operator fun plus(other: Matrix): Matrix {
        require(rows == other.rows && cols == other.cols) { "Dimension mismatch for addition" }
        val result = Array(rows) { DoubleArray(cols) }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                result[r][c] = data[r][c] + other.data[r][c]
            }
        }
        return Matrix(rows, cols, result)
    }

    operator fun minus(other: Matrix): Matrix {
        require(rows == other.rows && cols == other.cols) { "Dimension mismatch for subtraction" }
        val result = Array(rows) { DoubleArray(cols) }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                result[r][c] = data[r][c] - other.data[r][c]
            }
        }
        return Matrix(rows, cols, result)
    }

    operator fun times(other: Matrix): Matrix {
        require(cols == other.rows) { "Matrix multiplication dimension mismatch (${rows}x$cols * ${other.rows}x${other.cols})" }
        val result = Array(rows) { DoubleArray(other.cols) }
        for (i in 0 until rows) {
            for (j in 0 until other.cols) {
                var sum = 0.0
                for (k in 0 until cols) {
                    sum += data[i][k] * other.data[k][j]
                }
                result[i][j] = sum
            }
        }
        return Matrix(rows, other.cols, result)
    }

    operator fun times(scalar: Double): Matrix {
        val result = Array(rows) { DoubleArray(cols) }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                result[r][c] = data[r][c] * scalar
            }
        }
        return Matrix(rows, cols, result)
    }

    fun transpose(): Matrix {
        val result = Array(cols) { DoubleArray(rows) }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                result[c][r] = data[r][c]
            }
        }
        return Matrix(cols, rows, result)
    }

    val trace: Double get() {
        require(isSquare) { "Trace is only defined for square matrices" }
        var sum = 0.0
        for (i in 0 until rows) sum += data[i][i]
        return sum
    }

    fun determinant(): Double {
        require(isSquare) { "Determinant is only defined for square matrices" }
        return when (rows) {
            1 -> data[0][0]
            2 -> data[0][0] * data[1][1] - data[0][1] * data[1][0]
            3 -> {
                val a = data[0][0]; val b = data[0][1]; val c = data[0][2]
                val d = data[1][0]; val e = data[1][1]; val f = data[1][2]
                val g = data[2][0]; val h = data[2][1]; val i = data[2][2]
                a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g)
            }
            else -> gaussianDeterminant()
        }
    }

    private fun gaussianDeterminant(): Double {
        val n = rows
        val a = Array(n) { data[it].clone() }
        var det = 1.0
        var sign = 1.0

        for (i in 0 until n) {
            var pivot = i
            for (j in i + 1 until n) {
                if (abs(a[j][i]) > abs(a[pivot][i])) pivot = j
            }
            if (abs(a[pivot][i]) < 1e-12) return 0.0
            if (i != pivot) {
                val tmp = a[i]; a[i] = a[pivot]; a[pivot] = tmp
                sign = -sign
            }
            det *= a[i][i]
            for (j in i + 1 until n) {
                val factor = a[j][i] / a[i][i]
                for (k in i + 1 until n) {
                    a[j][k] -= factor * a[i][k]
                }
            }
        }
        return det * sign
    }

    fun inverse(): Matrix? {
        require(isSquare) { "Only square matrices can have an inverse" }
        val det = determinant()
        if (abs(det) < 1e-11) return null

        val n = rows
        val augmented = Array(n) { DoubleArray(2 * n) }
        for (i in 0 until n) {
            for (j in 0 until n) {
                augmented[i][j] = data[i][j]
            }
            augmented[i][n + i] = 1.0
        }

        // Gauss-Jordan elimination
        for (i in 0 until n) {
            var pivot = i
            for (j in i + 1 until n) {
                if (abs(augmented[j][i]) > abs(augmented[pivot][i])) pivot = j
            }
            if (abs(augmented[pivot][i]) < 1e-12) return null
            if (i != pivot) {
                val tmp = augmented[i]; augmented[i] = augmented[pivot]; augmented[pivot] = tmp
            }

            val div = augmented[i][i]
            for (k in 0 until 2 * n) {
                augmented[i][k] /= div
            }

            for (j in 0 until n) {
                if (j != i) {
                    val factor = augmented[j][i]
                    for (k in 0 until 2 * n) {
                        augmented[j][k] -= factor * augmented[i][k]
                    }
                }
            }
        }

        val inv = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0 until n) {
                inv[i][j] = augmented[i][n + j]
            }
        }
        return Matrix(n, n, inv)
    }

    fun power(p: Int): Matrix {
        require(isSquare) { "Matrix power requires square matrix" }
        require(p >= 1) { "Power must be >= 1" }
        var result = this
        for (i in 2..p) {
            result = result * this
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix) return false
        if (rows != other.rows || cols != other.cols) return false
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (abs(data[r][c] - other.data[r][c]) > 1e-9) return false
            }
        }
        return true
    }

    override fun hashCode(): Int = 31 * rows + cols

    fun format(precision: Int = 3): String {
        val sb = StringBuilder()
        for (r in 0 until rows) {
            sb.append("[ ")
            for (c in 0 until cols) {
                val v = data[r][c]
                val str = if (v == v.toLong().toDouble()) v.toLong().toString()
                else String.format(Locale.US, "%.${precision}f", v).trimEnd('0').trimEnd('.')
                sb.append(str).append(if (c < cols - 1) ", " else "")
            }
            sb.append(" ]\n")
        }
        return sb.toString().trimEnd()
    }

    companion object {
        fun identity(n: Int): Matrix {
            val d = Array(n) { DoubleArray(n) }
            for (i in 0 until n) d[i][i] = 1.0
            return Matrix(n, n, d)
        }

        fun zeros(r: Int, c: Int): Matrix = Matrix(r, c, Array(r) { DoubleArray(c) })
    }
}
