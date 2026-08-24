package com.ngel.appestadistica.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ngel.appestadistica.domain.model.DescriptiveStatistics
import com.ngel.appestadistica.domain.model.VariableType
import com.ngel.appestadistica.ui.StatisticsUiState
import com.ngel.appestadistica.ui.display
import com.ngel.appestadistica.ui.displayOrNA

@Composable
fun MainScreen(
    state: StatisticsUiState,
    onInputChange: (String) -> Unit,
    onVariableSelected: (VariableType) -> Unit,
    onCalculate: () -> Unit,
    onToggleStatistics: () -> Unit,
    onFrequencyClick: () -> Unit,
    onBoxPlotClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Estadística descriptiva", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        InputCard(state, onInputChange, onVariableSelected, onCalculate)
        NavigationCard("▣", "GENERAR tabla de frecuencias", onFrequencyClick)
        NavigationCard("↕", "GENERAR GRÁFICO DE CAJA O BIGOTE", onBoxPlotClick)
        state.statistics?.let { StatisticsCard(it, state.statisticsExpanded, onToggleStatistics) }
    }
}

@Composable
private fun InputCard(
    state: StatisticsUiState,
    onInputChange: (String) -> Unit,
    onVariableSelected: (VariableType) -> Unit,
    onCalculate: () -> Unit
) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Tipo de variable", fontWeight = FontWeight.SemiBold)
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                VariableType.entries.forEachIndexed { index, type ->
                    SegmentedButton(
                        selected = state.variableType == type,
                        onClick = { onVariableSelected(type) },
                        shape = SegmentedButtonDefaults.itemShape(index, VariableType.entries.size)
                    ) { Text(type.label) }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Ingresa los datos", fontWeight = FontWeight.SemiBold)
                Text("Separados por comas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedTextField(
                value = state.input,
                onValueChange = onInputChange,
                modifier = Modifier.fillMaxWidth().height(116.dp),
                placeholder = { Text("Ej.: 12, 15, 18, 20, 22") },
                supportingText = state.error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = state.error != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                maxLines = 4
            )
            Button(
                onClick = onCalculate, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("▣   CALCULAR") }
        }
    }
}

@Composable
private fun NavigationCard(symbol: String, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(horizontal = 18.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(symbol, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(14.dp))
            Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Text("›", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StatisticsCard(stats: DescriptiveStatistics, expanded: Boolean, onToggle: () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth().clickable(onClick = onToggle), verticalAlignment = Alignment.CenterVertically) {
                Text("Estadísticas", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(if (expanded) "⌃" else "⌄")
            }
            if (expanded) {
                Spacer(Modifier.height(12.dp))
                ModeValue(stats.modes)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    StatisticColumn(Modifier.weight(1f), listOf(
                        "Media" to stats.mean.display(), "Error estándar" to stats.standardError.displayOrNA(),
                        "Mediana" to stats.median.display(), "Primer cuartil" to stats.q1.display(),
                        "Tercer cuartil" to stats.q3.display(), "Varianza" to stats.variance.displayOrNA(),
                        "Desviación típica" to stats.standardDeviation.displayOrNA()
                    ))
                    Spacer(Modifier.width(16.dp))
                    StatisticColumn(Modifier.weight(1f), listOf(
                        "Curtosis" to stats.kurtosis.displayOrNA(), "Asimetría" to stats.skewness.displayOrNA(),
                        "Intervalo" to stats.range.display(), "Mínimo" to stats.min.display(), "Máximo" to stats.max.display(),
                        "Suma" to stats.sum.display(), "Recuento" to stats.count.display()
                    ))
                }
            }
        }
    }
}

@Composable
private fun ModeValue(modes: List<Double>) {
    val value = if (modes.isEmpty()) "Sin moda" else modes.joinToString(separator = ", ", prefix = "[", postfix = "]") { it.display() }
    Column(Modifier.fillMaxWidth()) {
        Text("Moda", style = MaterialTheme.typography.labelSmall)
        Text(
            value,
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatisticColumn(modifier: Modifier, values: List<Pair<String, String>>) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(7.dp)) {
        values.forEach { (label, value) ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                Text(value, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
