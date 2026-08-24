# Feature: coeficiente de variación

## Objetivo

Incorporar el **coeficiente de variación** como una estadística adicional en el resumen de resultados de la pantalla principal.

## Definición

El coeficiente de variación expresa la dispersión de los datos en relación con su media y se calcula como:

`CV = (desviación típica / media) × 100`

El resultado debe presentarse como porcentaje con cuatro decimales.

## Comportamiento esperado

- Mostrar el coeficiente de variación dentro de la sección desplegable **Estadísticas**.
- Usar la desviación típica muestral que ya calcula la aplicación.
- Para una media igual a cero, mostrar **No aplica** para evitar una división indefinida.
- El cálculo debe conservar la precisión interna y aplicar el formato de cuatro decimales únicamente al mostrar el resultado.

## Criterios de aceptación

1. Con datos cuya media sea distinta de cero, se muestra `CV = (desviación típica / media) × 100` con cuatro decimales y el símbolo `%`.
2. Con media igual a cero o una desviación típica no disponible, se muestra **No aplica**.
3. El valor utiliza la misma desviación típica muestral que aparece en el resumen actual.
4. La nueva estadística no altera los cálculos existentes.

## Estado

**Pendiente de implementación.**
