package com.ngel.appestadistica.domain

import com.ngel.appestadistica.domain.model.FrequencyTable
import com.ngel.appestadistica.domain.model.VariableType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StatisticsCalculatorTest {
    private val sample = listOf(12.0, 14.0, 15.0, 15.0, 17.0, 18.0, 20.0, 21.0, 21.0, 24.0)

    @Test fun `calculates sample statistics`() {
        val result = StatisticsCalculator.calculate(sample)
        assertEquals(17.7, result.mean, 0.0001)
        assertEquals(14.2333, result.variance!!, 0.0001)
        assertEquals(3.7727, result.standardDeviation!!, 0.0001)
        assertEquals(1.1930, result.standardError!!, 0.0001)
        assertEquals(listOf(15.0, 21.0), result.modes)
        assertEquals(15.0, result.q1, 0.0)
        assertEquals(17.5, result.median, 0.0)
        assertEquals(21.0, result.q3, 0.0)
    }

    @Test fun `parses common separators and rejects invalid values`() {
        val parsed = StatisticsCalculator.parseInput("1, 2; 3\n4") as ParseResult.Success
        assertEquals(listOf(1.0, 2.0, 3.0, 4.0), parsed.values)
        assertTrue(StatisticsCalculator.parseInput("1, hola") is ParseResult.Error)
    }

    @Test fun `marks outliers and chooses non outlier whiskers`() {
        val plot = StatisticsCalculator.calculateBoxPlot(listOf(1.0, 2.0, 3.0, 4.0, 5.0, 100.0))
        assertEquals(listOf(100.0), plot.outliers)
        assertEquals(1.0, plot.lowerWhisker, 0.0)
        assertEquals(5.0, plot.upperWhisker, 0.0)
    }

    @Test fun `frequency totals cover every observation`() {
        val discrete = FrequencyTableCalculator.calculate(sample, VariableType.DISCRETA) as FrequencyTable.Discrete
        assertEquals(sample.size, discrete.rows.sumOf { it.frequency })
        assertEquals(1.0, discrete.rows.last().accumulatedRelativeFrequency, 0.000001)
        val continuous = FrequencyTableCalculator.calculate(sample, VariableType.CONTINUA) as FrequencyTable.Continuous
        assertEquals(sample.size, continuous.rows.sumOf { it.frequency })
        assertEquals(1.0, continuous.rows.last().accumulatedRelativeFrequency, 0.000001)
    }
}
