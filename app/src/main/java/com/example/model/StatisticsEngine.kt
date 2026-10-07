package com.example.model

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class OneVarStats(
    val n: Int,
    val mean: Double,
    val sumX: Double,
    val sumX2: Double,
    val sampleStdDev: Double,
    val popStdDev: Double,
    val sampleVariance: Double,
    val popVariance: Double,
    val min: Double,
    val q1: Double,
    val median: Double,
    val q3: Double,
    val max: Double
)

data class TwoVarStats(
    val n: Int,
    val meanX: Double,
    val meanY: Double,
    val sumX: Double,
    val sumY: Double,
    val sumX2: Double,
    val sumY2: Double,
    val sumXY: Double,
    val a: Double, // y = a + bx
    val b: Double,
    val r: Double, // correlation coefficient
    val r2: Double
) {
    fun predictY(x: Double): Double = a + b * x
    fun predictX(y: Double): Double = if (abs(b) > 1e-12) (y - a) / b else Double.NaN
}

object StatisticsEngine {
    fun calculateOneVar(data: List<Pair<Double, Int>>): OneVarStats? {
        val flatList = mutableListOf<Double>()
        for ((value, freq) in data) {
            val count = if (freq <= 0) 1 else freq
            repeat(count) { flatList.add(value) }
        }
        if (flatList.isEmpty()) return null

        flatList.sort()
        val n = flatList.size
        val sumX = flatList.sum()
        val sumX2 = flatList.sumOf { it * it }
        val mean = sumX / n

        val popVariance = flatList.sumOf { (it - mean).pow(2) } / n
        val popStdDev = sqrt(popVariance)

        val sampleVariance = if (n > 1) flatList.sumOf { (it - mean).pow(2) } / (n - 1) else 0.0
        val sampleStdDev = sqrt(sampleVariance)

        val min = flatList.first()
        val max = flatList.last()
        val median = getPercentile(flatList, 50.0)
        val q1 = getPercentile(flatList, 25.0)
        val q3 = getPercentile(flatList, 75.0)

        return OneVarStats(
            n = n,
            mean = mean,
            sumX = sumX,
            sumX2 = sumX2,
            sampleStdDev = sampleStdDev,
            popStdDev = popStdDev,
            sampleVariance = sampleVariance,
            popVariance = popVariance,
            min = min,
            q1 = q1,
            median = median,
            q3 = q3,
            max = max
        )
    }

    private fun getPercentile(sorted: List<Double>, p: Double): Double {
        if (sorted.isEmpty()) return 0.0
        val index = (p / 100.0) * (sorted.size - 1)
        val lower = index.toInt()
        val fraction = index - lower
        return if (lower + 1 < sorted.size) {
            sorted[lower] + fraction * (sorted[lower + 1] - sorted[lower])
        } else {
            sorted[lower]
        }
    }

    fun calculateLinearRegression(points: List<Pair<Double, Double>>): TwoVarStats? {
        val n = points.size
        if (n < 2) return null

        val sumX = points.sumOf { it.first }
        val sumY = points.sumOf { it.second }
        val sumX2 = points.sumOf { it.first * it.first }
        val sumY2 = points.sumOf { it.second * it.second }
        val sumXY = points.sumOf { it.first * it.second }

        val meanX = sumX / n
        val meanY = sumY / n

        val denom = (n * sumX2 - sumX * sumX)
        if (abs(denom) < 1e-12) return null

        val b = (n * sumXY - sumX * sumY) / denom
        val a = meanY - b * meanX

        val denomR = sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY))
        val r = if (abs(denomR) > 1e-12) (n * sumXY - sumX * sumY) / denomR else 0.0

        return TwoVarStats(
            n = n,
            meanX = meanX,
            meanY = meanY,
            sumX = sumX,
            sumY = sumY,
            sumX2 = sumX2,
            sumY2 = sumY2,
            sumXY = sumXY,
            a = a,
            b = b,
            r = r,
            r2 = r * r
        )
    }

    fun permutations(n: Long, r: Long): Long {
        if (r < 0 || r > n) return 0L
        var res = 1L
        for (i in 0 until r) {
            res *= (n - i)
        }
        return res
    }

    fun combinations(n: Long, r: Long): Long {
        if (r < 0 || r > n) return 0L
        val k = if (r > n - r) n - r else r
        var res = 1L
        for (i in 1..k) {
            res = res * (n - i + 1) / i
        }
        return res
    }
}
