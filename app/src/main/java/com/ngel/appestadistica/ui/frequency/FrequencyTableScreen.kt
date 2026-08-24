package com.ngel.appestadistica.ui.frequency

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ngel.appestadistica.domain.FrequencyTableCalculator
import com.ngel.appestadistica.domain.model.FrequencyTable
import com.ngel.appestadistica.ui.StatisticsUiState
import com.ngel.appestadistica.ui.display

@Composable
fun FrequencyTableScreen(state: StatisticsUiState, onBack: () -> Unit) {
    val table = runCatching { FrequencyTableCalculator.calculate(state.data, state.variableType) }.getOrNull()
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tabla de frecuencia", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(14.dp)) {
                Text("Variable: ${state.variableType.label}", fontWeight = FontWeight.Bold)
                Text(if (state.variableType.name == "CONTINUA") "Agrupación por intervalos de clase (Sturges)" else "Valores únicos ordenados", style = MaterialTheme.typography.bodySmall)
            }
        }
        when (table) {
            is FrequencyTable.Discrete -> DiscreteTable(table)
            is FrequencyTable.Continuous -> ContinuousTable(table)
            null -> Text("No se pudo construir la tabla. Regresa e ingresa al menos dos datos para una variable continua.", color = MaterialTheme.colorScheme.error)
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("←   Volver a principal") }
    }
}

@Composable
private fun DiscreteTable(table: FrequencyTable.Discrete) {
    val scroll = rememberScrollState()
    Card(shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.horizontalScroll(scroll).padding(4.dp)) {
            TableRow(listOf("xi", "fi", "Fi", "hi", "Hi"), header = true)
            table.rows.forEach { row -> TableRow(listOf(row.value.display(), row.frequency.toString(), row.accumulatedFrequency.toString(), row.relativeFrequency.display(), row.accumulatedRelativeFrequency.display())) }
            TableRow(listOf("Total", table.total.toString(), table.total.toString(), "1.00", "1.00"), total = true)
        }
    }
}

@Composable
private fun ContinuousTable(table: FrequencyTable.Continuous) {
    val scroll = rememberScrollState()
    Card(shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.horizontalScroll(scroll).padding(4.dp)) {
            TableRow(listOf("Intervalo", "xi", "fi", "Fi", "hi", "Hi"), header = true)
            table.rows.forEach { row ->
                TableRow(listOf("${row.lowerLimit.display()} – ${row.upperLimit.display()}", row.classMark.display(), row.frequency.toString(), row.accumulatedFrequency.toString(), row.relativeFrequency.display(), row.accumulatedRelativeFrequency.display()))
            }
            TableRow(listOf("Total", "", table.total.toString(), table.total.toString(), "1.00", "1.00"), total = true)
        }
    }
}

@Composable
private fun TableRow(values: List<String>, header: Boolean = false, total: Boolean = false) {
    Row(Modifier.fillMaxWidth()) {
        values.forEachIndexed { index, value ->
            Text(
                value, modifier = Modifier.width(if (index == 0) 142.dp else 66.dp).padding(8.dp),
                fontWeight = if (header || total) FontWeight.Bold else FontWeight.Normal,
                color = if (header) MaterialTheme.colorScheme.onPrimary else if (total) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
