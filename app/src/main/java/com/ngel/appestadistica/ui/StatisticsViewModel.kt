package com.ngel.appestadistica.ui

import androidx.lifecycle.ViewModel
import com.ngel.appestadistica.domain.ParseResult
import com.ngel.appestadistica.domain.StatisticsCalculator
import com.ngel.appestadistica.domain.model.DescriptiveStatistics
import com.ngel.appestadistica.domain.model.VariableType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StatisticsUiState(
    val input: String = "",
    val variableType: VariableType = VariableType.CONTINUA,
    val data: List<Double> = emptyList(),
    val statistics: DescriptiveStatistics? = null,
    val error: String? = null,
    val statisticsExpanded: Boolean = true
)

class StatisticsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    fun updateInput(value: String) {
        _uiState.value = _uiState.value.copy(input = value, error = null)
    }

    fun selectVariableType(type: VariableType) {
        _uiState.value = _uiState.value.copy(variableType = type, error = null)
    }

    fun toggleStatistics() {
        _uiState.value = _uiState.value.copy(statisticsExpanded = !_uiState.value.statisticsExpanded)
    }

    fun calculate() {
        val data = validatedData() ?: return
        _uiState.value = _uiState.value.copy(data = data, statistics = StatisticsCalculator.calculate(data), error = null)
    }

    fun validateForNavigation(): Boolean {
        val data = validatedData() ?: return false
        _uiState.value = _uiState.value.copy(data = data, error = null)
        return true
    }

    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }

    private fun validatedData(): List<Double>? = when (val parsed = StatisticsCalculator.parseInput(_uiState.value.input)) {
        is ParseResult.Success -> parsed.values
        is ParseResult.Error -> {
            _uiState.value = _uiState.value.copy(error = parsed.message)
            null
        }
    }
}
