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
        val q1 = medianOf(sorted.take(count / 2))
        val median = medianOf(sorted)
        val q3 = medianOf(sorted.drop((count + 1) / 2))
        val modes = modesOf(sorted)
        val variance = if (count >= 2) sorted.sumOf { (it - mean) * (it - mean) } / (count - 1) else null
        val standardDeviation = variance?.let(::sqrt)
        val standardError = standardDeviation?.div(sqrt(count.toDouble()))
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
        return DescriptiveStatistics(mean, standardError, modes, median, q1, q3, variance, standardDeviation, kurtosis, skewness, max - min, min, max, sum, count)
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

    private fun medianOf(sorted: List<Double>): Double {
        require(sorted.isNotEmpty())
        val middle = sorted.size / 2
        return if (sorted.size % 2 == 0) (sorted[middle - 1] + sorted[middle]) / 2.0 else sorted[middle]
    }

    private fun modesOf(values: List<Double>): List<Double> {
        val frequencies = values.groupingBy { it }.eachCount()
        val maximum = frequencies.values.maxOrNull() ?: 0
        return if (maximum <= 1) emptyList() else frequencies.filterValues { it == maximum }.keys.sorted()
    }
}
