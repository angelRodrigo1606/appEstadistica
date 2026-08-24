# Feature: mostrar estadísticas con cuatro decimales

## Objetivo

Actualizar la presentación de los resultados estadísticos para que los valores numéricos se muestren con **cuatro decimales**.

## Alcance

La regla debe aplicarse al cuadro de estadísticas de la pantalla principal y a los valores numéricos derivados que se visualicen en las demás pantallas cuando corresponda, por ejemplo:

- Media.
- Error estándar.
- Mediana y cuartiles.
- Varianza y desviación típica.
- Curtosis y asimetría.
- Rango, mínimo, máximo y suma.
- Frecuencias relativas y acumuladas.
- Límites, marcas de clase y valores del gráfico de caja.

Los valores enteros como **Recuento**, `fi` y `Fi` deben conservarse sin decimales.

## Comportamiento esperado

- Un resultado como `17.7` debe mostrarse como `17.7000`.
- Un resultado entero como `24` debe mostrarse como `24.0000` cuando represente una medida estadística.
- Los resultados no aplicables deben continuar mostrándose como `No aplica`.
- La precisión visual no debe alterar los cálculos internos ni redondear los datos antes de procesarlos.

## Criterios de aceptación

1. Dado un cálculo estadístico válido, cada medida decimal se muestra con exactamente cuatro cifras decimales.
2. Los conteos y frecuencias absolutas se muestran como números enteros.
3. Las pantallas de tabla de frecuencias y gráfico de caja mantienen el mismo formato de cuatro decimales para sus valores no enteros.
4. La aplicación conserva la precisión de cálculo original; el formato se aplica únicamente al presentar el resultado.

## Estado

**Solucionado.** El formateador centralizado de valores decimales usa ahora el patrón `0.0000`. Las pantallas principal, de frecuencias y de gráfico de caja lo reutilizan, por lo que muestran cuatro decimales de forma consistente. Los valores de conteo y frecuencias absolutas continúan presentándose como enteros.
