package com.ngel.appestadistica

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ngel.appestadistica.ui.StatisticsViewModel
import com.ngel.appestadistica.ui.boxplot.BoxPlotScreen
import com.ngel.appestadistica.ui.frequency.FrequencyTableScreen
import com.ngel.appestadistica.ui.main.MainScreen
import com.ngel.appestadistica.ui.position.PositionMeasuresScreen

private const val MAIN_ROUTE = "principal"
private const val FREQUENCY_ROUTE = "frecuencias"
private const val BOX_PLOT_ROUTE = "caja"
private const val POSITION_MEASURES_ROUTE = "medidas_posicion"

@Composable
fun EstadisticaApp(viewModel: StatisticsViewModel = viewModel()) {
    val navController = rememberNavController()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    NavHost(navController = navController, startDestination = MAIN_ROUTE) {
        composable(MAIN_ROUTE) {
            MainScreen(
                state = state,
                onInputChange = viewModel::updateInput,
                onVariableSelected = viewModel::selectVariableType,
                onCalculate = viewModel::calculate,
                onToggleStatistics = viewModel::toggleStatistics,
                onFrequencyClick = { if (viewModel.validateForNavigation()) navController.navigate(FREQUENCY_ROUTE) },
                onBoxPlotClick = { if (viewModel.validateForNavigation()) navController.navigate(BOX_PLOT_ROUTE) },
                onPositionMeasuresClick = { if (viewModel.validateForNavigation()) navController.navigate(POSITION_MEASURES_ROUTE) }
            )
        }
        composable(FREQUENCY_ROUTE) { FrequencyTableScreen(state = state, onBack = navController::popBackStack) }
        composable(BOX_PLOT_ROUTE) { BoxPlotScreen(state = state, onBack = navController::popBackStack) }
        composable(POSITION_MEASURES_ROUTE) { PositionMeasuresScreen(data = state.data, onBack = navController::popBackStack) }
    }
}
