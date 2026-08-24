# Plan de implementación — App de Estadística Descriptiva

## 1. Objetivo

Convertir la aplicación Android existente, desarrollada con Kotlin y Jetpack Compose, en una calculadora de estadística descriptiva para datos numéricos. La app permitirá ingresar una muestra, obtener sus estadísticos, consultar una tabla de frecuencias y visualizar un diagrama de caja y bigotes.

Este plan se basa en [ERS.md](ERS.md) y [mockupApp.png](mockupApp.png). El alcance es local y en memoria: no incluye autenticación, base de datos ni exportación.

## 2. Estado actual

- Proyecto Android Compose con `minSdk 31` y Material 3.
- Solo existe la pantalla de ejemplo en `MainActivity.kt`.
- Aún no hay navegación, `ViewModel`, lógica estadística ni pruebas de dominio.

## 3. Alcance funcional

| Pantalla | Resultado esperado |
|---|---|
| Principal | Selector Continua/Discreta, área de texto, cálculo y tarjeta expandible de estadísticas. |
| Tabla de frecuencias | Tabla discreta por valores únicos o continua por intervalos; botón para volver. |
| Gráfico de caja | Caja horizontal, bigotes, eje con escala, resumen de cinco números y aviso de atípicos. |

Las tres acciones validan la entrada. El estado se conserva al navegar para permitir regresar, editar y recalcular.

## 4. Decisiones técnicas propuestas

### Arquitectura

- Aplicar una estructura simple MVVM: UI Compose → `StatisticsViewModel` → casos de cálculo puros.
- Mantener un único `StatisticsUiState` como fuente de verdad: texto original, tipo de variable, datos parseados, estadísticas, error y estado de expansión.
- Usar `SavedStateHandle` o `rememberSaveable` para sobrevivir cambios de configuración. Para una primera versión, el estado no se guardará al cerrar completamente la app.
- Usar Navigation Compose para las rutas `principal`, `frecuencias` y `caja`; los resultados se derivan del mismo estado, no se pasan como objetos serializados entre rutas.

### Dependencias a incorporar

- `androidx.navigation:navigation-compose` para la navegación.
- `androidx.lifecycle:lifecycle-viewmodel-compose` para obtener el `ViewModel` desde Compose.

No se requiere una librería externa de gráficos: el box plot se dibujará con `Canvas`, lo que mantiene el proyecto liviano y permite respetar el mockup.

### Contratos de dominio

```kotlin
enum class VariableType { CONTINUA, DISCRETA }

data class DescriptiveStatistics(/* 15 valores mostrables */)
data class FrequencyRow(/* intervalo/xi, fi, Fi, hi, Hi */)
data class BoxPlotSummary(/* Q1, mediana, Q3, bigotes y atípicos */)
```

Los resultados que no tengan sentido matemático se representarán como `null` en dominio y como `"No aplica"` en la interfaz, en vez de inventar un cero.

## 5. Reglas de cálculo que deben quedar fijadas

El mockup aporta decisiones concretas para el conjunto `12, 14, 15, 15, 17, 18, 20, 21, 21, 24`:

- La pantalla del mockup muestra valores de dispersión de referencia, pero no son consistentes con los datos visibles. La implementación usa **varianza muestral** (divisor `n - 1`), una convención habitual para una muestra.
- `Q1 = 15`, mediana `17.50` y `Q3 = 21`: corresponden al método de **mediana de mitades** (Tukey), excluyendo la mediana cuando `n` es impar.
- Curtosis `-0.95` y asimetría `0.21`: implementar las versiones **muestrales corregidas de sesgo** y curtosis como exceso (equivalentes a `KURT` y `SKEW` de Excel), documentándolo en código y pruebas.
- Un conjunto sin repeticiones no tiene moda: mostrar `"Sin moda"`. Si hay empate, mostrar todas las modas como `[x, y]`.
- Formatear resultados con 2 decimales por defecto; el recuento y las frecuencias absolutas no llevan decimales.

Para `n = 1`, mostrar media, moda, mediana, Q1, Q3, mínimo, máximo, suma y recuento; las medidas que requieran dispersión se mostrarán como no aplicables. Para asimetría y curtosis, exigir respectivamente `n >= 3` y `n >= 4`.

## 6. Plan por etapas

### Etapa 1 — Base del proyecto y tema visual

1. Reemplazar el ejemplo `Greeting` por el punto de entrada `EstadisticaApp`.
2. Ajustar tema Material 3: fondo claro, azul primario, superficies blancas, bordes suaves y tipografía legible, siguiendo el mockup.
3. Agregar navegación y definir las tres rutas.
4. Crear componentes reutilizables: `PrimaryButton`, `NavigationActionCard`, `SectionCard`, `BackButton` y formateadores numéricos.

**Criterio de salida:** las tres pantallas son navegables con datos simulados y se adaptan a un teléfono en vertical.

### Etapa 2 — Dominio estadístico y validación

1. Implementar `parseInput(raw: String)`:
   - aceptar comas, saltos de línea y espacios entre valores;
   - aceptar punto decimal en cada valor;
   - informar la posición o el valor inválido si falla;
   - rechazar datos vacíos, infinitos y `NaN`.
2. Implementar funciones puras para ordenamiento, media, suma, mínimo, máximo, rango, moda, mediana, cuartiles, varianza, desviación, error estándar, asimetría y curtosis.
3. Implementar `calculateBoxPlot`, con RIC, límites de 1.5 × RIC, bigotes como extremos no atípicos y lista de outliers.
4. Crear `StatisticsViewModel` con eventos de edición, selección, cálculo y validación previa a navegar.

**Criterio de salida:** todos los cálculos se ejecutan fuera de la UI y las entradas inválidas no permiten continuar.

### Etapa 3 — Pantalla principal

1. Implementar selector segmentado de tipo de variable, iniciando en Continua como el mockup.
2. Añadir `OutlinedTextField` multilínea con etiqueta, ejemplo y soporte de desplazamiento.
3. Implementar botón azul **CALCULAR**.
4. Mostrar la tarjeta **Estadísticas** expandible con dos columnas y los 15 indicadores del ERS.
5. Añadir las dos tarjetas de navegación: tabla de frecuencias y gráfico de caja.
6. Presentar errores mediante texto accesible junto al campo y/o `Snackbar`.

**Criterio de salida:** el caso de ejemplo del mockup produce sus valores visibles, incluida la moda `[15, 21]`.

### Etapa 4 — Tabla de frecuencias

1. Para variable discreta, agrupar valores únicos ordenados y calcular `fi`, `Fi`, `hi` y `Hi`.
2. Para variable continua, usar Sturges: `k = round(1 + 3.322 × log10(n))`; definir un mínimo de dos datos para esta vista.
3. Calcular amplitud `c = rango / k`; si el rango es cero, mostrar un único intervalo para evitar divisiones inválidas.
4. Construir intervalos consecutivos de tipo `[LI, LS)` y hacer inclusivo el límite superior de la última clase, asegurando que cada dato se cuente una sola vez.
5. Mostrar encabezado contextual (tipo y método), tabla horizontalmente desplazable y fila de total.

**Criterio de salida:** la suma de `fi` es `n` y el último `Hi` es 1.00 en ambos tipos de variable.

### Etapa 5 — Gráfico de caja y bigotes

1. Crear `BoxPlotChart` con `Canvas` y dimensiones responsivas.
2. Transformar valores de dominio a coordenadas de pantalla con margen; extender el eje si todos los valores son iguales.
3. Dibujar eje y marcas, bigotes, caja Q1–Q3, línea de mediana y atípicos rojos.
4. Añadir etiquetas superiores de mínimo, Q1, mediana, Q3 y máximo no atípico; incluir el panel resumen inferior y aviso de atípicos del mockup.
5. Añadir semántica accesible con el resumen textual del gráfico. El tooltip táctil queda como mejora opcional posterior.

**Criterio de salida:** el gráfico distingue los bigotes de los atípicos y coincide con los estadísticos calculados.

### Etapa 6 — Calidad y cierre

1. Pruebas unitarias para parser y funciones estadísticas, con casos pares/impares, modas múltiples, datos iguales, negativos, decimales y muestras pequeñas.
2. Pruebas de tabla discreta y continua: cobertura completa, acumulados y límites.
3. Pruebas de UI Compose para validación, cálculo, navegación y retorno conservando los datos.
4. Verificación manual en emulador o dispositivo: fuentes, contraste, scroll, orientación, tamaños de pantalla y rendimiento con 1,000 valores.

**Criterio de salida:** el proyecto compila, las pruebas pasan y cada criterio de aceptación del ERS está verificado.

## 7. Estructura de archivos propuesta

```text
app/src/main/java/com/ngel/appestadistica/
├── MainActivity.kt
├── App.kt
├── data/                         # reservado para persistencia futura
├── domain/
│   ├── model/StatisticsModels.kt
│   ├── StatisticsCalculator.kt
│   ├── FrequencyTableCalculator.kt
│   └── BoxPlotCalculator.kt
├── ui/
│   ├── navigation/AppNavigation.kt
│   ├── main/MainScreen.kt
│   ├── frequency/FrequencyTableScreen.kt
│   ├── boxplot/BoxPlotScreen.kt
│   ├── components/...
│   ├── viewmodel/StatisticsViewModel.kt
│   └── theme/...
└── util/NumberFormatter.kt
```

Las pruebas unitarias acompañarán el paquete `domain`; las pruebas de interfaz se ubicarán en `androidTest`.

## 8. Riesgos y decisiones pendientes

| Tema | Propuesta inicial | Validación pendiente |
|---|---|---|
| Decimales | Punto como salida, hasta 2 decimales por defecto. | Confirmar si la interfaz debe usar coma decimal para el público objetivo. |
| Entrada con coma decimal | Interpretar `12,5` como dos datos para conservar el separador exigido por ERS. | Si se necesita coma decimal, incorporar un selector/formato inequívoco (por ejemplo `;` entre valores). |
| Intervalos continuos | Regla de Sturges, intervalos semiabiertos y último extremo cerrado. | Confirmar el redondeo visual de límites y amplitud. |
| Curtosis y asimetría | Fórmulas muestrales corregidas, coherentes con el mockup. | Confirmar con el docente o responsable funcional. |
| Persistencia | Solo memoria y restauración de configuración. | Definir si se requiere conservar datos después de cerrar la app. |

## 9. Matriz de trazabilidad resumida

| Requerimientos | Entregable principal |
|---|---|
| RF-01 a RF-06, RF-14 | `MainScreen`, parser, `StatisticsCalculator`, `StatisticsViewModel` |
| RF-07 a RF-09 | `FrequencyTableScreen`, `FrequencyTableCalculator` |
| RF-10 a RF-11 | `BoxPlotScreen`, `BoxPlotCalculator`, `BoxPlotChart` |
| RF-12 a RF-13 | Navigation Compose y estado compartido en `ViewModel` |
| RNF-01, RNF-05, RNF-08 | Componentes Compose, semántica, layout responsive |
| RNF-02 a RNF-03, RNF-07 | Cálculos puros, pruebas unitarias y formateo centralizado |

## 10. Orden recomendado de ejecución

1. Añadir dependencias y navegación.
2. Construir y probar el dominio matemático antes de conectar la interfaz.
3. Completar la pantalla principal y el estado compartido.
4. Implementar tabla de frecuencias.
5. Implementar el gráfico de caja con Canvas.
6. Ejecutar pruebas y ajustar el diseño frente al mockup.

Este orden reduce retrabajo: las tres pantallas consumen los mismos datos ya validados y los mismos cálculos centrales.
