package com.ngel.appestadistica.domain.model

enum class VariableType(val label: String) {
    CONTINUA("Continua"),
    DISCRETA("Discreta")
}

data class DescriptiveStatistics(
    val mean: Double,
    val standardError: Double?,
    val modes: List<Double>,
    val median: Double,
    val q1: Double,
    val q3: Double,
    val variance: Double?,
    val standardDeviation: Double?,
    val kurtosis: Double?,
    val skewness: Double?,
    val range: Double,
    val min: Double,
    val max: Double,
    val sum: Double,
    val count: Int
)

data class DiscreteFrequencyRow(
    val value: Double,
    val frequency: Int,
    val accumulatedFrequency: Int,
    val relativeFrequency: Double,
    val accumulatedRelativeFrequency: Double
)

data class ContinuousFrequencyRow(
    val lowerLimit: Double,
    val upperLimit: Double,
    val classMark: Double,
    val frequency: Int,
    val accumulatedFrequency: Int,
    val relativeFrequency: Double,
    val accumulatedRelativeFrequency: Double
)

sealed interface FrequencyTable {
    val total: Int

    data class Discrete(
        val rows: List<DiscreteFrequencyRow>,
        override val total: Int
    ) : FrequencyTable

    data class Continuous(
        val rows: List<ContinuousFrequencyRow>,
        val intervalCount: Int,
        val classWidth: Double,
        override val total: Int
    ) : FrequencyTable
}

data class BoxPlotSummary(
    val min: Double,
    val q1: Double,
    val median: Double,
    val q3: Double,
    val max: Double,
    val lowerWhisker: Double,
    val upperWhisker: Double,
    val lowerFence: Double,
    val upperFence: Double,
    val outliers: List<Double>
)
