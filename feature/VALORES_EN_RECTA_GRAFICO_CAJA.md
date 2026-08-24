# Feature: mostrar valores en la recta del gráfico de caja

## Objetivo

Agregar etiquetas numéricas en la recta de referencia del gráfico de caja y bigotes para facilitar la lectura de sus medidas principales.

## Problema

El gráfico representa visualmente los bigotes, la caja, la mediana y los valores atípicos, pero no muestra los valores numéricos asociados a los puntos de la recta. La persona debe inferirlos a partir de la posición o consultar otro apartado.

## Comportamiento esperado

- Mostrar en la recta del gráfico las referencias numéricas de mínimo, primer cuartil, mediana, tercer cuartil y máximo.
- Usar los valores de los bigotes cuando existan valores atípicos, de modo que la escala del gráfico represente correctamente los límites visualizados.
- Mantener el formato de cuatro decimales utilizado en el resto de la aplicación.
- Evitar superposiciones: las etiquetas deben conservar una lectura clara en pantallas pequeñas y con valores cercanos.

## Criterios de aceptación

1. El gráfico muestra etiquetas para los límites inferior y superior del bigote, Q1, mediana y Q3.
2. Las etiquetas se corresponden con los valores usados para dibujar cada elemento del gráfico.
3. Los valores atípicos no reemplazan las etiquetas de los límites de los bigotes.
4. Las etiquetas son legibles y no se superponen de forma que impida entenderlas.
5. Los resultados numéricos se muestran con cuatro decimales.

## Estado

**Solucionado.** El gráfico muestra bajo la recta los valores de los límites de bigotes, Q1, mediana y Q3. Cuando los valores están próximos, las etiquetas se distribuyen en filas para conservar la legibilidad.
