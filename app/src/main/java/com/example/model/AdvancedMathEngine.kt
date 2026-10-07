package com.example.model

import java.util.Locale
import kotlin.math.*

/**
 * Advanced mathematical computations for the 2nd mode features:
 * - Number Theory: Prime factorization, Divisors, IsPrime, GCD, LCM
 * - Linear Algebra: Trace, Det, Transpose, Inverse, Rank, RREF (RowReduce), Eigenvalues
 * - Vector Calculus: Dot, Cross, Angle, Distance, Norm, Normalize, Projection
 * - Special Functions: Gamma function
 */
object AdvancedMathEngine {

    // --- NUMBER THEORY ---

    fun isPrime(n: Long): Boolean {
        if (n <= 1) return false
        if (n <= 3) return true
        if (n % 2 == 0L || n % 3 == 0L) return false
        var i = 5L
        while (i * i <= n) {
            if (n % i == 0L || n % (i + 2) == 0L) return false
            i += 6L
        }
        return true
    }

    fun primeFactors(n: Long): String {
        var num = abs(n)
        if (num <= 1) return num.toString()
        val factors = mutableMapOf<Long, Int>()

        while (num % 2 == 0L) {
            factors[2] = (factors[2] ?: 0) + 1
            num /= 2
        }
        var d = 3L
        while (d * d <= num) {
            while (num % d == 0L) {
                factors[d] = (factors[d] ?: 0) + 1
                num /= d
            }
            d += 2
        }
        if (num > 1) {
            factors[num] = (factors[num] ?: 0) + 1
        }

        return factors.entries.joinToString(" × ") { (factor, count) ->
            if (count > 1) "$factor^$count" else "$factor"
        }
    }

    fun divisors(n: Long): List<Long> {
        val num = abs(n)
        if (num == 0L) return listOf(0L)
        val result = mutableListOf<Long>()
        var i = 1L
        while (i * i <= num) {
            if (num % i == 0L) {
                result.add(i)
                if (i * i != num) result.add(num / i)
            }
            i++
        }
        return result.sorted()
    }

    // --- SPECIAL FUNCTIONS ---

    fun gamma(z: Double): Double {
        if (z < 0.5) {
            return Math.PI / (sin(Math.PI * z) * gamma(1.0 - z))
        }
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

    // --- VECTOR OPERATIONS ---

    fun vectorAngle(u: MathVector, v: MathVector): Double = u.angleDeg(v)

    fun euclidDistance(u: MathVector, v: MathVector): Double {
        require(u.dim == v.dim) { "Dimension mismatch" }
        var sum = 0.0
        for (i in 0 until u.dim) {
            val diff = u[i] - v[i]
            sum += diff * diff
        }
        return sqrt(sum)
    }

    fun projection(u: MathVector, v: MathVector): MathVector {
        val vNormSq = v.norm * v.norm
        require(vNormSq > 1e-12) { "Cannot project onto zero vector" }
        val scalar = (u dot v) / vNormSq
        return v * scalar
    }

    // --- LINEAR ALGEBRA ---

    fun rank(matrix: Matrix): Int {
        val r = matrix.rows
        val c = matrix.cols
        val a = Array(r) { matrix.data[it].clone() }
        var rank = 0

        for (col in 0 until c) {
            var pivot = rank
            while (pivot < r && abs(a[pivot][col]) < 1e-10) pivot++
            if (pivot < r) {
                val tmp = a[rank]; a[rank] = a[pivot]; a[pivot] = tmp
                val div = a[rank][col]
                for (j in col until c) a[rank][j] /= div
                for (i in 0 until r) {
                    if (i != rank && abs(a[i][col]) > 1e-10) {
                        val factor = a[i][col]
                        for (j in col until c) a[i][j] -= factor * a[rank][j]
                    }
                }
                rank++
            }
            if (rank == r) break
        }
        return rank
    }

    fun rowReduce(matrix: Matrix): Matrix {
        val r = matrix.rows
        val c = matrix.cols
        val a = Array(r) { matrix.data[it].clone() }
        var lead = 0

        for (row in 0 until r) {
            if (lead >= c) break
            var i = row
            while (abs(a[i][lead]) < 1e-10) {
                i++
                if (i == r) {
                    i = row
                    lead++
                    if (lead == c) return Matrix(r, c, a)
                }
            }
            val tmp = a[i]; a[i] = a[row]; a[row] = tmp
            val div = a[row][lead]
            if (abs(div) > 1e-10) {
                for (j in 0 until c) a[row][j] /= div
            }
            for (k in 0 until r) {
                if (k != row) {
                    val factor = a[k][lead]
                    for (j in 0 until c) a[k][j] -= factor * a[row][j]
                }
            }
            lead++
        }
        return Matrix(r, c, a)
    }

    fun eigenvalues2x2(matrix: Matrix): List<ComplexNumber> {
        require(matrix.rows == 2 && matrix.cols == 2) { "Requires 2x2 matrix" }
        val tr = matrix.trace
        val det = matrix.determinant()
        val delta = tr * tr - 4.0 * det
        return if (delta >= 0) {
            listOf(
                ComplexNumber((tr + sqrt(delta)) / 2.0, 0.0),
                ComplexNumber((tr - sqrt(delta)) / 2.0, 0.0)
            )
        } else {
            val real = tr / 2.0
            val imag = sqrt(-delta) / 2.0
            listOf(ComplexNumber(real, imag), ComplexNumber(real, -imag))
        }
    }

    fun diagonal(matrix: Matrix): MathVector {
        val n = min(matrix.rows, matrix.cols)
        val diag = DoubleArray(n) { matrix[it, it] }
        return MathVector(diag)
    }
}
