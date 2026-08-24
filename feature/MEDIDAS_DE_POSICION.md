# Feature: medidas de posición

## Objetivo

Incorporar una pantalla para consultar cuartiles, deciles y percentiles de los datos ingresados.

## Alcance

- Presentar Q1, Q2 y Q3.
- Presentar D1 a D9.
- Permitir consultar un percentil P1 a P99 a la vez.
- Calcular todas las medidas con la posición `k(n + 1) / 100`, usando interpolación lineal.
- Si la posición no es entera, calcular `Pk = Xi + d × (Xi+1 - Xi)`; si es entera, tomar el valor de esa posición.
- Cuando la posición sea menor que el primer dato o mayor que el último, usar el extremo correspondiente.
- Mostrar los resultados con cuatro decimales.

## Criterios de aceptación

1. La pantalla solo se abre con datos válidos.
2. Los cuartiles coinciden con P25, P50 y P75.
3. Los deciles D1 a D9 se calculan correctamente.
4. La consulta rechaza valores vacíos, no numéricos o fuera de P1–P99 con un mensaje claro.

## Estado

**Implementado.**
