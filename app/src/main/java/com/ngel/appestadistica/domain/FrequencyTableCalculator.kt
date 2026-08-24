package com.ngel.appestadistica.domain

import com.ngel.appestadistica.domain.model.ContinuousFrequencyRow
import com.ngel.appestadistica.domain.model.DiscreteFrequencyRow
import com.ngel.appestadistica.domain.model.FrequencyTable
import com.ngel.appestadistica.domain.model.VariableType
import kotlin.math.log10
import kotlin.math.roundToInt

object FrequencyTableCalculator {
    fun calculate(values: List<Double>, type: VariableType): FrequencyTable = when (type) {
        VariableType.DISCRETA -> discrete(values)
        VariableType.CONTINUA -> continuous(values)
    }

    private fun discrete(values: List<Double>): FrequencyTable.Discrete {
        val total = values.size
        var accumulated = 0
        var accumulatedRelative = 0.0
        val rows = values.groupingBy { it }.eachCount().toSortedMap().map { (value, frequency) ->
            accumulated += frequency
            accumulatedRelative += frequency.toDouble() / total
            DiscreteFrequencyRow(value, frequency, accumulated, frequency.toDouble() / total, accumulatedRelative)
        }
        return FrequencyTable.Discrete(rows, total)
    }

    private fun continuous(values: List<Double>): FrequencyTable.Continuous {
        require(values.size >= 2) { "Se requieren al menos dos datos para agrupar una variable continua." }
        val min = values.min()
        val max = values.max()
        if (min == max) {
            val row = ContinuousFrequencyRow(min, max, min, values.size, values.size, 1.0, 1.0)
            return FrequencyTable.Continuous(listOf(row), 1, 0.0, values.size)
        }
        val intervalCount = (1 + 3.322 * log10(values.size.toDouble())).roundToInt().coerceAtLeast(1)
        val classWidth = (max - min) / intervalCount
        val frequencies = IntArray(intervalCount)
        values.forEach { value ->
            val index = (((value - min) / classWidth).toInt()).coerceIn(0, intervalCount - 1)
            frequencies[index]++
        }
        var accumulated = 0
        var accumulatedRelative = 0.0
        val rows = frequencies.indices.map { index ->
            val lower = min + index * classWidth
            val upper = if (index == intervalCount - 1) max else min + (index + 1) * classWidth
            val frequency = frequencies[index]
            accumulated += frequency
            accumulatedRelative += frequency.toDouble() / values.size
            ContinuousFrequencyRow(lower, upper, (lower + upper) / 2, frequency, accumulated, frequency.toDouble() / values.size, accumulatedRelative)
        }
        return FrequencyTable.Continuous(rows, intervalCount, classWidth, values.size)
    }
}
