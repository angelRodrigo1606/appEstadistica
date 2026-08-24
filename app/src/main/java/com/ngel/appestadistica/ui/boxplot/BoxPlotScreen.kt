package com.ngel.appestadistica.ui.boxplot

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ngel.appestadistica.domain.StatisticsCalculator
import com.ngel.appestadistica.domain.model.BoxPlotSummary
import com.ngel.appestadistica.ui.StatisticsUiState
import com.ngel.appestadistica.ui.display

@Composable
fun BoxPlotScreen(state: StatisticsUiState, onBack: () -> Unit) {
    val plot = state.data.takeIf { it.isNotEmpty() }?.let(StatisticsCalculator::calculateBoxPlot)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Gráfico de caja", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (plot == null) {
            Text("No hay datos para graficar.", color = MaterialTheme.colorScheme.error)
        } else {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    SummaryLabels(plot)
                    BoxPlotChart(plot, Modifier.fillMaxWidth().height(250.dp))
                    Text("Valor", style = MaterialTheme.typography.labelMedium)
                }
            }
            FiveNumberCard(plot)
            if (plot.outliers.isEmpty()) Text("No se detectaron valores atípicos.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            else Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Text("Se detecta${if (plot.outliers.size == 1) "" else "n"} ${plot.outliers.size} valor${if (plot.outliers.size == 1) "" else "es"} atípico${if (plot.outliers.size == 1) "" else "s"}: ${plot.outliers.joinToString { it.display() }}", Modifier.padding(14.dp))
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("←   Volver a principal") }
    }
}

@Composable
private fun SummaryLabels(plot: BoxPlotSummary) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("Mín" to plot.lowerWhisker, "Q1" to plot.q1, "Mediana" to plot.median, "Q3" to plot.q3, "Máx" to plot.upperWhisker).forEach { (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Text(value.display(), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun FiveNumberCard(plot: BoxPlotSummary) {
    Card(shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Mínimo" to plot.lowerWhisker, "Q1" to plot.q1, "Mediana" to plot.median, "Q3" to plot.q3, "Máximo" to plot.upperWhisker).forEach { (label, value) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(label, style = MaterialTheme.typography.labelSmall)
                    Text(value.display(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BoxPlotChart(plot: BoxPlotSummary, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    Canvas(modifier) {
        val domainMin = minOf(plot.min, plot.lowerFence)
        val domainMax = maxOf(plot.max, plot.upperFence)
        val padding = (domainMax - domainMin).takeIf { it > 0 }?.times(0.1) ?: 1.0
        val start = domainMin - padding
        val end = domainMax + padding
        fun x(value: Double) = ((value - start) / (end - start) * (size.width - 24.dp.toPx()) + 12.dp.toPx()).toFloat()
        val middle = size.height * .30f
        val boxTop = middle - 30.dp.toPx()
        val boxBottom = middle + 30.dp.toPx()
        val axisY = size.height * .56f
        drawLine(Color.Gray, Offset(12.dp.toPx(), axisY), Offset(size.width - 12.dp.toPx(), axisY), 2.dp.toPx())
        drawLine(Color.DarkGray, Offset(x(plot.lowerWhisker), middle), Offset(x(plot.q1), middle), 3.dp.toPx())
        drawLine(Color.DarkGray, Offset(x(plot.q3), middle), Offset(x(plot.upperWhisker), middle), 3.dp.toPx())
        drawLine(Color.DarkGray, Offset(x(plot.lowerWhisker), boxTop + 12.dp.toPx()), Offset(x(plot.lowerWhisker), boxBottom - 12.dp.toPx()), 3.dp.toPx())
        drawLine(Color.DarkGray, Offset(x(plot.upperWhisker), boxTop + 12.dp.toPx()), Offset(x(plot.upperWhisker), boxBottom - 12.dp.toPx()), 3.dp.toPx())
        drawRect(primary.copy(alpha = .18f), Offset(x(plot.q1), boxTop), androidx.compose.ui.geometry.Size(x(plot.q3) - x(plot.q1), boxBottom - boxTop))
        drawRect(primary, Offset(x(plot.q1), boxTop), androidx.compose.ui.geometry.Size(x(plot.q3) - x(plot.q1), boxBottom - boxTop), style = Stroke(2.dp.toPx()))
        drawLine(primary, Offset(x(plot.median), boxTop), Offset(x(plot.median), boxBottom), 3.dp.toPx())
        plot.outliers.forEach { drawCircle(Color(0xFFD32F2F), 6.dp.toPx(), Offset(x(it), middle)) }
        repeat(6) { index ->
            val tickX = 12.dp.toPx() + index * (size.width - 24.dp.toPx()) / 5
            drawLine(Color.Gray, Offset(tickX, axisY), Offset(tickX, axisY - 7.dp.toPx()), 1.dp.toPx())
        }
        drawAxisValueLabels(
            values = listOf(plot.lowerWhisker, plot.q1, plot.median, plot.q3, plot.upperWhisker),
            axisY = axisY,
            xForValue = ::x
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAxisValueLabels(
    values: List<Double>,
    axisY: Float,
    xForValue: (Double) -> Float
) {
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.DKGRAY
        textSize = 10.sp.toPx()
    }
    val sidePadding = 4.dp.toPx()
    val gap = 6.dp.toPx()
    val rowHeight = 14.dp.toPx()
    val rowEnds = mutableListOf<Float>()
    val labels = values.distinct().sorted().map { value ->
        val text = value.display()
        val width = textPaint.measureText(text)
        val left = (xForValue(value) - width / 2).coerceIn(sidePadding, size.width - width - sidePadding)
        val row = rowEnds.indexOfFirst { left >= it + gap }.takeIf { it >= 0 } ?: rowEnds.size
        if (row == rowEnds.size) rowEnds += left + width else rowEnds[row] = left + width
        Triple(text, left, row)
    }
    drawIntoCanvas { canvas ->
        labels.forEach { (text, left, row) ->
            canvas.nativeCanvas.drawText(text, left, axisY + 18.dp.toPx() + row * rowHeight, textPaint)
        }
    }
}
