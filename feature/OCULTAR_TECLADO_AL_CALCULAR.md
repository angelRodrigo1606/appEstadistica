# Feature: ocultar el teclado al calcular

## Objetivo

Mejorar la experiencia de uso de la pantalla principal ocultando el teclado numérico cuando la persona pulsa el botón **CALCULAR**.

## Problema

Al presionar **CALCULAR**, el teclado numérico permanece visible y puede cubrir parte de las estadísticas resultantes o requerir que la persona lo cierre manualmente antes de revisar los resultados.

## Comportamiento esperado

- Al pulsar **CALCULAR**, el campo de entrada de datos debe perder el foco y el teclado debe ocultarse.
- La acción debe ocurrir tanto si el cálculo es válido como si se muestra un mensaje de validación.
- El cálculo, los mensajes de error y la visualización de estadísticas deben conservar su comportamiento actual.
- La aplicación no debe intentar ocultar el teclado si no está visible, ni producir errores en ese caso.

## Criterios de aceptación

1. Con el teclado numérico visible, al pulsar **CALCULAR** este se oculta de inmediato.
2. Si los datos son válidos, las estadísticas quedan visibles sin que el teclado las cubra.
3. Si los datos son inválidos, se muestra el error correspondiente y el teclado permanece oculto.
4. La interacción es compatible con dispositivos que no tengan un teclado virtual activo.

## Estado

**Solucionado.** Al pulsar **CALCULAR**, la pantalla principal limpia el foco del campo de datos y solicita ocultar el teclado virtual antes de ejecutar la validación y el cálculo.
