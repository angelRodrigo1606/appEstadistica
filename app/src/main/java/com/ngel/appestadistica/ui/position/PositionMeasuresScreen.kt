package com.ngel.appestadistica.ui.position

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ngel.appestadistica.domain.StatisticsCalculator
import com.ngel.appestadistica.ui.display
import kotlinx.coroutines.launch

@Composable
fun PositionMeasuresScreen(data: List<Double>, onBack: () -> Unit) {
    val quartiles = remember(data) {
        listOf(
            "Q1 (P25)" to StatisticsCalculator.percentileInclusive(data, 0.25),
            "Q2 (P50)" to StatisticsCalculator.percentileInclusive(data, 0.50),
            "Q3 (P75)" to StatisticsCalculator.percentileInclusive(data, 0.75)
        )
    }
    val deciles = remember(data) {
        (1..9).map { index -> "D$index (P${index * 10})" to StatisticsCalculator.percentileInclusive(data, index / 10.0) }
    }
    var percentileInput by rememberSaveable { mutableStateOf("") }
    var queryResult by rememberSaveable { mutableStateOf<Double?>(null) }
    var queryError by rememberSaveable { mutableStateOf<String?>(null) }
    val percentileCardRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Medidas de posición", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Resultados calculados con la posición k(n + 1) / 100.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        MeasureCard("Cuartiles", quartiles)
        MeasureCard("Deciles", deciles)
        Card(
            modifier = Modifier.bringIntoViewRequester(percentileCardRequester),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Consultar percentil", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = percentileInput,
                    onValueChange = { percentileInput = it; queryError = null; queryResult = null },
                    modifier = Modifier.fillMaxWidth()
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch { percentileCardRequester.bringIntoView() }
                            }
                        },
                    label = { Text("Percentil entre 1 y 99") },
                    placeholder = { Text("Ej.: 85") },
                    isError = queryError != null,
                    supportingText = queryError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                Button(onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    val percentile = percentileInput.replace(',', '.').toDoubleOrNull()
                    when {
                        percentile == null -> queryError = "Ingresa un percentil numérico entre 1 y 99."
                        percentile !in 1.0..99.0 -> queryError = "El percentil debe estar entre 1 y 99."
                        else -> {
                            queryResult = StatisticsCalculator.percentileInclusive(data, percentile / 100.0)
                            queryError = null
                        }
                    }
                }, modifier = Modifier.fillMaxWidth()) { Text("CALCULAR PERCENTIL") }
                queryResult?.let { Text("P${percentileInput} = ${it.display()}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("←   Volver a principal") }
    }
}

@Composable
private fun MeasureCard(title: String, measures: List<Pair<String, Double>>) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            measures.forEach { (label, value) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label)
                    Text(value.display(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
