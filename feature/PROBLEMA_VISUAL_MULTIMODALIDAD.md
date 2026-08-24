# Problema visual: visualización de múltiples modas

## Descripción

Cuando el conjunto de datos tiene varias modas, el valor mostrado en el campo **Moda** de la tarjeta de estadísticas no dispone de espacio horizontal suficiente. Como consecuencia, el texto se ajusta de forma excesiva y cada número —o incluso cada carácter— puede aparecer en una línea distinta.

Esto dificulta la lectura del resultado y rompe la alineación visual de la tarjeta de estadísticas.

## Comportamiento actual

- El campo **Moda** intenta mostrar la lista completa de valores modales, por ejemplo: `[12.00, 15.00, 18.00, 21.00]`.
- En pantallas estrechas o cuando hay muchas modas, el texto se fragmenta verticalmente dentro de la columna.

## Comportamiento esperado

- La lista de modas debe conservar una lectura natural, con los valores agrupados y separados por comas.
- El texto no debe dividirse carácter por carácter.
- Si el espacio disponible no es suficiente, la interfaz debe priorizar una solución legible: ampliar la fila, permitir el salto por valores completos, usar desplazamiento horizontal o mostrar el detalle en una vista expandible.

## Criterio de aceptación

Dado un conjunto de datos con tres o más modas, al calcular las estadísticas, el valor de **Moda** debe poder leerse completo y ordenadamente sin que sus caracteres aparezcan en líneas independientes.

## Estado

**Solucionado.** El campo **Moda** ahora se muestra en una fila de ancho completo dentro de la tarjeta de estadísticas. Esto evita que compita por espacio con la segunda columna y permite que una lista extensa se ajuste entre valores separados por comas, manteniendo una lectura ordenada.
