package com.ngel.appestadistica.domain

import com.ngel.appestadistica.domain.model.BoxPlotSummary
import com.ngel.appestadistica.domain.model.DescriptiveStatistics
import kotlin.math.sqrt

sealed interface ParseResult {
    data class Success(val values: List<Double>) : ParseResult
    data class Error(val message: String) : ParseResult
}

object StatisticsCalculator {
    fun parseInput(raw: String): ParseResult {
        val text = raw.trim()
        if (text.isEmpty()) return ParseResult.Error("Ingresa al menos un dato para continuar.")
        val tokens = text.split(Regex("[,;\\s]+"))
        val values = mutableListOf<Double>()
        tokens.forEachIndexed { index, token ->
            val value = token.toDoubleOrNull()
            if (value == null || !value.isFinite()) {
                return ParseResult.Error("El valor ${index + 1} ('$token') no es un número válido.")
            }
            values += value
        }
        return ParseResult.Success(values)
    }

    fun calculate(values: List<Double>): DescriptiveStatistics {
        require(values.isNotEmpty())
        val sorted = values.sorted()
        val count = sorted.size
        val sum = sorted.sum()
        val mean = sum / count
        val min = sorted.first()
        val max = sorted.last()
        val q1 = percentileInclusive(sorted, 0.25)
        val median = percentileInclusive(sorted, 0.50)
        val q3 = percentileInclusive(sorted, 0.75)
        val modes = modesOf(sorted)
        val variance = if (count >= 2) sorted.sumOf { (it - mean) * (it - mean) } / (count - 1) else null
        val standardDeviation = variance?.let(::sqrt)
        val standardError = standardDeviation?.div(sqrt(count.toDouble()))
        val coefficientVariation = standardDeviation?.takeIf { mean != 0.0 }?.let { deviation ->
            (deviation / mean) * 100.0
        }
        val skewness = standardDeviation?.takeIf { count >= 3 && it > 0.0 }?.let { deviation ->
            count.toDouble() / ((count - 1.0) * (count - 2.0)) *
                sorted.sumOf { ((it - mean) / deviation).let { standardized -> standardized * standardized * standardized } }
        }
        val kurtosis = standardDeviation?.takeIf { count >= 4 && it > 0.0 }?.let { deviation ->
            val n = count.toDouble()
            val fourthMoment = sorted.sumOf { ((it - mean) / deviation).let { standardized -> standardized * standardized * standardized * standardized } }
            (n * (n + 1.0) / ((n - 1.0) * (n - 2.0) * (n - 3.0))) * fourthMoment -
                (3.0 * (n - 1.0) * (n - 1.0) / ((n - 2.0) * (n - 3.0)))
        }
        return DescriptiveStatistics(mean, standardError, modes, median, q1, q3, variance, standardDeviation, coefficientVariation, kurtosis, skewness, max - min, min, max, sum, count)
    }

    fun calculateBoxPlot(values: List<Double>): BoxPlotSummary {
        require(values.isNotEmpty())
        val stats = calculate(values)
        val iqr = stats.q3 - stats.q1
        val lowerFence = stats.q1 - 1.5 * iqr
        val upperFence = stats.q3 + 1.5 * iqr
        val sorted = values.sorted()
        val nonOutliers = sorted.filter { it >= lowerFence && it <= upperFence }
        return BoxPlotSummary(
            min = stats.min, q1 = stats.q1, median = stats.median, q3 = stats.q3, max = stats.max,
            lowerWhisker = nonOutliers.first(), upperWhisker = nonOutliers.last(),
            lowerFence = lowerFence, upperFence = upperFence,
            outliers = sorted.filter { it < lowerFence || it > upperFence }
        )
    }

    /**
     * Calcula un percentil mediante la posición k(n + 1) / 100.
     * Si la posición no es entera, interpola como Xi + d(Xi+1 - Xi).
     * [percentile] se expresa entre 0.0 (P0) y 1.0 (P100).
     */
    fun percentileInclusive(values: List<Double>, percentile: Double): Double {
        require(values.isNotEmpty()) { "Se requiere al menos un dato." }
        require(percentile in 0.0..1.0) { "El percentil debe estar entre 0 y 1." }
        val sorted = values.sorted()
        val oneBasedPosition = percentile * (sorted.size + 1)
        if (oneBasedPosition <= 1.0) return sorted.first()
        if (oneBasedPosition >= sorted.size) return sorted.last()
        val lowerPosition = oneBasedPosition.toInt()
        if (oneBasedPosition == lowerPosition.toDouble()) return sorted[lowerPosition - 1]
        val lowerIndex = lowerPosition - 1
        val upperIndex = lowerIndex + 1
        val fraction = oneBasedPosition - lowerPosition
        return sorted[lowerIndex] + (sorted[upperIndex] - sorted[lowerIndex]) * fraction
    }

    private fun modesOf(values: List<Double>): List<Double> {
        val frequencies = values.groupingBy { it }.eachCount()
        val maximum = frequencies.values.maxOrNull() ?: 0
        return if (maximum <= 1) emptyList() else frequencies.filterValues { it == maximum }.keys.sorted()
    }
}
