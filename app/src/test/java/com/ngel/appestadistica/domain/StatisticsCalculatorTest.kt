package com.ngel.appestadistica.domain

import com.ngel.appestadistica.domain.model.FrequencyTable
import com.ngel.appestadistica.domain.model.VariableType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StatisticsCalculatorTest {
    private val sample = listOf(12.0, 14.0, 15.0, 15.0, 17.0, 18.0, 20.0, 21.0, 21.0, 24.0)

    @Test fun `calculates sample statistics`() {
        val result = StatisticsCalculator.calculate(sample)
        assertEquals(17.7, result.mean, 0.0001)
        assertEquals(14.2333, result.variance!!, 0.0001)
        assertEquals(3.7727, result.standardDeviation!!, 0.0001)
        assertEquals(result.standardDeviation!! / result.mean * 100.0, result.coefficientVariation!!, 0.0001)
        assertEquals(1.1930, result.standardError!!, 0.0001)
        assertEquals(listOf(15.0, 21.0), result.modes)
        assertEquals(14.75, result.q1, 0.0)
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

    @Test fun `calculates percentiles with n plus one position and linear interpolation`() {
        val values = listOf(4.0, 1.0, 3.0, 2.0)
        assertEquals(1.25, StatisticsCalculator.percentileInclusive(values, 0.25), 0.0001)
        assertEquals(2.5, StatisticsCalculator.percentileInclusive(values, 0.50), 0.0001)
        assertEquals(3.75, StatisticsCalculator.percentileInclusive(values, 0.75), 0.0001)
        assertEquals(2.0, StatisticsCalculator.percentileInclusive(values, 0.40), 0.0001)
        assertEquals(1.0, StatisticsCalculator.percentileInclusive(values, 0.01), 0.0001)
        assertEquals(4.0, StatisticsCalculator.percentileInclusive(values, 0.99), 0.0001)
        assertEquals(1.5, StatisticsCalculator.percentileInclusive(listOf(1.0, 2.0, 3.0, 4.0, 5.0), 0.25), 0.0001)
    }

    @Test fun `quartiles match percentiles calculated with n plus one`() {
        val values = listOf(1.0, 2.0, 3.0, 4.0)
        val statistics = StatisticsCalculator.calculate(values)
        assertEquals(StatisticsCalculator.percentileInclusive(values, 0.25), statistics.q1, 0.0)
        assertEquals(StatisticsCalculator.percentileInclusive(values, 0.50), statistics.median, 0.0)
        assertEquals(StatisticsCalculator.percentileInclusive(values, 0.75), statistics.q3, 0.0)
        assertEquals(1.0, StatisticsCalculator.percentileInclusive(values, 0.10), 0.0001)
        assertEquals(4.0, StatisticsCalculator.percentileInclusive(values, 0.90), 0.0001)
    }

    @Test fun `does not calculate variation coefficient when mean is zero`() {
        val result = StatisticsCalculator.calculate(listOf(-2.0, 2.0))
        assertNull(result.coefficientVariation)
    }
}
