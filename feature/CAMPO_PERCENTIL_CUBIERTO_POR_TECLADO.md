# Feature: mantener visible el campo de percentil con el teclado abierto

## Objetivo

Evitar que el campo de entrada para consultar un percentil quede cubierto por el teclado virtual en la pantalla **Medidas de posición**.

## Problema

Al tocar el campo para ingresar un percentil, el teclado numérico puede superponerse al campo de texto o al botón de cálculo. Esto dificulta confirmar el valor escrito y completar la consulta.

## Comportamiento esperado

- Al recibir el foco, el campo de percentil debe desplazarse a una zona visible por encima del teclado.
- El botón **CALCULAR PERCENTIL** debe permanecer visible y accesible junto con el campo mientras el teclado esté abierto.
- Al pulsar **CALCULAR PERCENTIL**, el campo debe perder el foco y el teclado numérico debe ocultarse antes de validar y mostrar el resultado.
- El resultado de la consulta debe seguir siendo accesible mediante desplazamiento vertical.
- El comportamiento debe funcionar en pantallas de distintos tamaños y con el teclado numérico activo.
- La pantalla debe conservar la posición de desplazamiento natural al cerrar el teclado, sin ocultar resultados ni generar saltos bruscos.

## Criterios de aceptación

1. Con el teclado abierto, el campo de percentil es completamente visible.
2. Con el campo enfocado, el botón **CALCULAR PERCENTIL** se muestra completo y puede pulsarse sin cerrar el teclado manualmente.
3. Al pulsar el botón, el teclado numérico se oculta tanto para resultados válidos como para errores de validación.
4. Los mensajes de error y el resultado del percentil pueden consultarse desplazando la pantalla si es necesario.
5. El cambio no modifica los cálculos ni la validación de P1 a P99.

## Estado

**Solucionado.** La pantalla reserva espacio para el teclado y lleva a la vista la tarjeta completa de consulta, incluido el botón. Al pulsar **CALCULAR PERCENTIL**, limpia el foco y oculta el teclado antes de validar el valor.
