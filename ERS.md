# Especificación de Requerimientos de Software (ERS)
## Sistema de Análisis Estadístico Descriptivo

*Módulo de Estadística Descriptiva, Tabla de Frecuencias y Diagrama de Caja y Bigotes*

- **Versión:** 1.0
- **Fecha de elaboración:** 23 de agosto de 2026
- **Estado del documento:** Borrador para revisión

---

## Control de versiones

| Versión | Fecha | Descripción | Autor |
|---|---|---|---|
| 1.0 | 23/08/2026 | Versión inicial del documento de requerimientos, elaborada a partir de la especificación funcional de las tres pantallas del sistema. | Equipo de Análisis |

---

## Tabla de contenido

1. [Introducción](#1-introducción)
   - 1.1 [Propósito](#11-propósito)
   - 1.2 [Alcance](#12-alcance)
   - 1.3 [Definiciones, siglas y abreviaturas](#13-definiciones-siglas-y-abreviaturas)
2. [Descripción general del sistema](#2-descripción-general-del-sistema)
   - 2.1 [Perspectiva del producto](#21-perspectiva-del-producto)
   - 2.2 [Funciones principales del producto](#22-funciones-principales-del-producto)
   - 2.3 [Usuarios del sistema](#23-usuarios-del-sistema)
   - 2.4 [Suposiciones y dependencias](#24-suposiciones-y-dependencias)
3. [Requerimientos funcionales](#3-requerimientos-funcionales)
4. [Especificación detallada de pantallas](#4-especificación-detallada-de-pantallas)
   - 4.1 [Pantalla 1 — Principal](#41-pantalla-1--principal)
   - 4.2 [Pantalla 2 — Tabla de Frecuencias](#42-pantalla-2--tabla-de-frecuencias)
   - 4.3 [Pantalla 3 — Gráfico de Caja y Bigotes](#43-pantalla-3--gráfico-de-caja-y-bigotes)
5. [Cuadro de estadísticas — detalle de cálculo (RF-05)](#5-cuadro-de-estadísticas--detalle-de-cálculo-rf-05)
6. [Validaciones y manejo de errores](#6-validaciones-y-manejo-de-errores)
7. [Requerimientos no funcionales](#7-requerimientos-no-funcionales)
8. [Flujo de navegación entre pantallas](#8-flujo-de-navegación-entre-pantallas)
9. [Criterios de aceptación generales](#9-criterios-de-aceptación-generales)

---

## 1. Introducción

### 1.1 Propósito

El presente documento tiene como propósito describir de manera detallada los requerimientos funcionales y no funcionales del Sistema de Análisis Estadístico Descriptivo, el cual permite a un usuario ingresar un conjunto de datos, calcular sus principales medidas de estadística descriptiva, generar su tabla de frecuencias y visualizar su diagrama de caja y bigotes (box plot).

Este documento servirá como base para el diseño, desarrollo, pruebas y validación del sistema, y como referencia para todos los interesados (equipo de desarrollo, control de calidad y usuario final).

### 1.2 Alcance

El sistema estará compuesto por tres (3) pantallas principales:

- **Pantalla 1 – Principal:** permite seleccionar el tipo de variable, ingresar los datos, ejecutar el cálculo de estadísticas descriptivas y acceder a la tabla de frecuencias y al gráfico de caja.
- **Pantalla 2 – Tabla de Frecuencias:** presenta la tabla de frecuencias construida a partir de los datos ingresados, aplicando el método correspondiente según el tipo de variable (continua o discreta).
- **Pantalla 3 – Gráfico de Caja:** presenta el diagrama de caja y bigotes (box plot) construido a partir de los estadísticos de posición calculados.

El sistema no contempla, salvo indicación adicional, el almacenamiento persistente de los datos en una base de datos, la gestión de usuarios, ni la exportación de resultados a otros formatos; estos podrán definirse como requerimientos futuros.

### 1.3 Definiciones, siglas y abreviaturas

| Término | Definición |
|---|---|
| Variable discreta | Variable estadística que solo puede tomar valores dentro de un conjunto numerable (contable), generalmente números enteros. |
| Variable continua | Variable estadística que puede tomar cualquier valor dentro de un intervalo o rango numérico. |
| Tabla de frecuencias | Tabla que resume la distribución de un conjunto de datos, indicando cuántas veces se repite cada valor o intervalo de valores. |
| Intervalo de clase | Rango de valores en el que se agrupan los datos de una variable continua para construir la tabla de frecuencias. |
| Diagrama de caja y bigotes (Box Plot) | Representación gráfica que resume la distribución de un conjunto de datos a partir de sus cuartiles, mediana y valores atípicos. |
| Outlier / valor atípico | Dato que se encuentra significativamente alejado del resto de las observaciones. |
| ERS | Especificación de Requerimientos de Software. |
| RF | Requerimiento Funcional. |
| RNF | Requerimiento No Funcional. |

---

## 2. Descripción general del sistema

### 2.1 Perspectiva del producto

El sistema es una aplicación de tipo calculadora estadística, orientada a estudiantes, docentes o analistas que requieran obtener rápidamente estadísticos descriptivos, la tabla de frecuencias y la representación gráfica (caja y bigotes) de un conjunto de datos numéricos ingresado manualmente.

### 2.2 Funciones principales del producto

- Selección del tipo de variable a analizar (continua o discreta).
- Ingreso manual de un arreglo de datos numéricos.
- Cálculo automático de un conjunto de estadísticos descriptivos.
- Generación de la tabla de frecuencias adaptada al tipo de variable.
- Generación del diagrama de caja y bigotes.

### 2.3 Usuarios del sistema

El sistema está dirigido a un único tipo de usuario (usuario final), sin roles diferenciados ni niveles de acceso, quien interactúa directamente con las tres pantallas descritas en este documento.

### 2.4 Suposiciones y dependencias

- Se asume que el usuario ingresará únicamente valores numéricos, separados por comas, en el TextArea de la Pantalla Principal.
- Se asume que existe al menos un dato válido para poder ejecutar los cálculos; en caso contrario, el sistema deberá informar el error correspondiente (ver sección 6).
- La cantidad mínima de datos para el cálculo de ciertos estadísticos (p. ej. curtosis, asimetría) puede requerir validaciones adicionales, las cuales se detallan en la sección 5.

---

## 3. Requerimientos funcionales

A continuación, se listan los requerimientos funcionales (RF) identificados a partir de las tres pantallas del sistema.

| ID | Nombre | Descripción | Prioridad |
|---|---|---|---|
| RF-01 | Selector de tipo de variable | El sistema debe permitir al usuario seleccionar, mediante un control tipo selector (dropdown o radio button), si la variable a analizar es Continua o Discreta. | Alta |
| RF-02 | Ingreso de datos | El sistema debe presentar un área de texto (TextArea) donde el usuario pueda ingresar un arreglo de datos numéricos separados por comas (ej. 12, 15, 18, 20, 22). | Alta |
| RF-03 | Validación de datos de entrada | El sistema debe validar que el contenido del TextArea corresponda a valores numéricos separados por comas. Ante datos vacíos, no numéricos o mal formateados, debe mostrarse un mensaje de error y no ejecutar el cálculo. | Alta |
| RF-04 | Botón CALCULAR | Al presionar el botón CALCULAR, el sistema debe procesar el arreglo de datos ingresado y completar automáticamente el cuadro de estadísticas ubicado en la parte inferior de la Pantalla Principal. | Alta |
| RF-05 | Cuadro de estadísticas descriptivas | El sistema debe calcular y mostrar, para el arreglo de datos ingresado, los siguientes 15 estadísticos: Media, Error estándar, Moda, Mediana, Primer cuartil (Q1), Tercer cuartil (Q3), Varianza, Desviación típica, Curtosis, Asimetría, Intervalo (Rango), Mínimo, Máximo, Suma y Recuento. | Alta |
| RF-06 | Manejo de multimodalidad | Si el conjunto de datos presenta más de un valor modal (multimodal), el sistema debe mostrar todas las modas identificadas en formato de arreglo/lista dentro del campo Moda. | Media |
| RF-07 | Botón GENERAR TABLA DE FRECUENCIAS | Al presionar este botón, el sistema debe construir la tabla de frecuencias a partir de los datos ingresados y del tipo de variable seleccionada, y navegar a la Pantalla 2 para mostrarla. | Alta |
| RF-08 | Tabla de frecuencias — variable discreta | Cuando la variable sea Discreta, el sistema debe construir la tabla de frecuencias utilizando los valores únicos del arreglo (sin agrupar), calculando para cada valor: frecuencia absoluta (fi), frecuencia absoluta acumulada (Fi), frecuencia relativa (hi) y frecuencia relativa acumulada (Hi). | Alta |
| RF-09 | Tabla de frecuencias — variable continua | Cuando la variable sea Continua, el sistema debe agrupar los datos en intervalos de clase (método de agrupación en clases, p. ej. regla de Sturges), calculando: número de intervalos (k), amplitud o ancho de clase (c), límites inferior y superior de cada intervalo, marca de clase (xi), frecuencia absoluta (fi), frecuencia absoluta acumulada (Fi), frecuencia relativa (hi) y frecuencia relativa acumulada (Hi). | Alta |
| RF-10 | Botón GENERAR GRÁFICO DE CAJA O BIGOTE | Al presionar este botón, el sistema debe calcular los estadísticos de posición necesarios (mínimo, Q1, mediana, Q3, máximo) y navegar a la Pantalla 3 para renderizar el diagrama de caja y bigotes correspondiente. | Alta |
| RF-11 | Detección de valores atípicos en el gráfico de caja | El sistema debe identificar y marcar en el diagrama, si existieran, los valores atípicos (outliers) del conjunto de datos, considerando los límites calculados a partir del rango intercuartílico (RIC = Q3 − Q1). | Media |
| RF-12 | Persistencia de datos entre pantallas | El arreglo de datos y el tipo de variable seleccionados en la Pantalla Principal deben conservarse al navegar hacia la Pantalla 2 o la Pantalla 3, sin requerir que el usuario los reingrese. | Alta |
| RF-13 | Navegación de retorno | Las Pantallas 2 y 3 deben proveer una opción para que el usuario regrese a la Pantalla Principal. | Media |
| RF-14 | Actualización ante cambio de datos | Si el usuario modifica el arreglo de datos o el tipo de variable después de haber generado resultados, el sistema debe permitir recalcular las estadísticas, la tabla de frecuencias y el gráfico de caja con la nueva información al presionar nuevamente los botones correspondientes. | Media |

---

## 4. Especificación detallada de pantallas

### 4.1 Pantalla 1 — Principal

Vista principal de la aplicación desde donde el usuario configura y ejecuta el análisis estadístico.

**4.1.1 Elementos de interfaz**

| Elemento | Descripción funcional |
|---|---|
| Selector de tipo de variable | Control de selección única con dos opciones: "Continua" y "Discreta". Determina el método de cálculo de la tabla de frecuencias (RF-08 / RF-09). |
| TextArea de datos | Campo de texto multilínea para el ingreso del arreglo de datos numéricos, separados por comas. Debe indicar mediante un texto de ayuda (placeholder) el formato esperado, por ejemplo: "Ingrese los datos separados por coma. Ej: 12,15,18,20,22". |
| Botón CALCULAR | Ejecuta el cálculo de los estadísticos descriptivos y actualiza el cuadro de estadísticas (RF-04, RF-05). |
| Botón GENERAR TABLA DE FRECUENCIAS | Redirige a la Pantalla 2 y genera la tabla de frecuencias (RF-07). |
| Botón GENERAR GRÁFICO DE CAJA O BIGOTE | Redirige a la Pantalla 3 y genera el diagrama de caja y bigotes (RF-10). |
| Cuadro de estadísticas | Tabla o panel de solo lectura, ubicado en la parte inferior de la pantalla, que muestra los 15 estadísticos calculados (ver detalle en sección 5). |

**4.1.2 Reglas de interacción**

- Los botones GENERAR TABLA DE FRECUENCIAS y GENERAR GRÁFICO DE CAJA O BIGOTE deben poder ejecutarse de forma independiente al botón CALCULAR, siempre que existan datos válidos ingresados.
- El cuadro de estadísticas solo se completa/actualiza mediante el botón CALCULAR.
- Si no se ha ingresado un arreglo de datos válido, los tres botones de acción deben mostrar un mensaje de validación en lugar de ejecutar el cálculo (ver sección 6).

### 4.2 Pantalla 2 — Tabla de Frecuencias

Se accede al presionar el botón GENERAR TABLA DE FRECUENCIAS de la Pantalla Principal. Muestra la tabla de frecuencias calculada, cuya estructura depende del tipo de variable seleccionado.

**4.2.1 Caso: variable discreta**

La tabla debe presentar una fila por cada valor único del arreglo (ordenado de menor a mayor), con las siguientes columnas:

| Columna | Descripción |
|---|---|
| xi | Valor de la variable (dato único). |
| fi | Frecuencia absoluta: número de veces que se repite el valor xi. |
| Fi | Frecuencia absoluta acumulada. |
| hi | Frecuencia relativa: fi / n. |
| Hi | Frecuencia relativa acumulada. |

**4.2.2 Caso: variable continua (agrupación en intervalos de clase)**

Cuando la variable es continua, los datos deben agruparse en intervalos de clase antes de construir la tabla. El sistema debe aplicar el siguiente procedimiento de cálculo:

- Determinar el rango: Rango = Máximo − Mínimo.
- Determinar el número de intervalos (k), por ejemplo mediante la regla de Sturges: k = 1 + 3,322 × log₁₀(n), redondeando al entero más cercano.
- Determinar la amplitud o ancho de clase: c = Rango / k.
- Construir los límites (inferior y superior) de cada intervalo de clase, de forma consecutiva y sin traslapes.
- Calcular la marca de clase de cada intervalo: xi = (límite inferior + límite superior) / 2.

La tabla resultante debe presentar las siguientes columnas:

| Columna | Descripción |
|---|---|
| Intervalo de clase | Límite inferior – límite superior del intervalo. |
| xi (marca de clase) | Punto medio del intervalo. |
| fi | Frecuencia absoluta: cantidad de datos que caen dentro del intervalo. |
| Fi | Frecuencia absoluta acumulada. |
| hi | Frecuencia relativa: fi / n. |
| Hi | Frecuencia relativa acumulada. |

> **Nota:** el método de agrupación (regla de cálculo de k y c) debe quedar configurado como parámetro documentado del sistema, de manera que el equipo de desarrollo defina y valide la fórmula exacta a implementar (Sturges u otra regla equivalente) durante el diseño técnico.

### 4.3 Pantalla 3 — Gráfico de Caja y Bigotes

Se accede al presionar el botón GENERAR GRÁFICO DE CAJA O BIGOTE de la Pantalla Principal. Presenta el diagrama de caja (box plot) construido a partir del arreglo de datos ingresado.

**4.3.1 Elementos del gráfico**

- Línea o marca del valor mínimo (dentro del límite inferior de bigote).
- Bigote inferior, desde el mínimo (no atípico) hasta el límite inferior de la caja (Q1).
- Caja delimitada por el Primer cuartil (Q1) y el Tercer cuartil (Q3).
- Línea de la Mediana dentro de la caja.
- Bigote superior, desde el límite superior de la caja (Q3) hasta el valor máximo (no atípico).
- Marcas individuales para los valores atípicos (outliers), si existieran, calculados con el criterio: outlier si el dato es menor a Q1 − 1,5×RIC o mayor a Q3 + 1,5×RIC, donde RIC = Q3 − Q1.

**4.3.2 Reglas de presentación**

- El gráfico debe incluir un eje numérico de referencia con escala acorde al rango de los datos.
- Al pasar el cursor sobre cada elemento del gráfico (mínimo, Q1, mediana, Q3, máximo, outliers), se recomienda mostrar su valor exacto (tooltip), como mejora de usabilidad.

---

## 5. Cuadro de estadísticas — detalle de cálculo (RF-05)

La siguiente tabla describe, para cada estadístico requerido en la Pantalla Principal, su definición conceptual y la fórmula/base de cálculo que el sistema debe implementar.

| Estadístico | Definición | Base de cálculo |
|---|---|---|
| Media | Promedio aritmético del conjunto de datos. | Suma de todos los valores dividida entre el recuento total (n). |
| Error estándar | Medida de la variabilidad de la media muestral. | Desviación típica dividida entre la raíz cuadrada de n. |
| Moda | Valor(es) que se repite(n) con mayor frecuencia. | Valor(es) con mayor frecuencia absoluta; si existe más de uno, se listan todos (multimodal). |
| Mediana | Valor central del conjunto de datos ordenado. | Con datos ordenados: valor central (n impar) o promedio de los dos valores centrales (n par). |
| Primer cuartil (Q1) | Valor que deja el 25% de los datos por debajo. | Percentil 25 del conjunto de datos ordenado. |
| Tercer cuartil (Q3) | Valor que deja el 75% de los datos por debajo. | Percentil 75 del conjunto de datos ordenado. |
| Varianza | Medida de dispersión respecto a la media. | Promedio de las diferencias al cuadrado entre cada dato y la media. |
| Desviación típica | Medida de dispersión en las mismas unidades del dato. | Raíz cuadrada de la varianza. |
| Curtosis | Medida del grado de concentración/apuntamiento de la distribución. | Momento de cuarto orden estandarizado respecto a la desviación típica. |
| Asimetría | Medida del grado de simetría de la distribución respecto a la media. | Momento de tercer orden estandarizado respecto a la desviación típica. |
| Intervalo (Rango) | Amplitud total del conjunto de datos. | Máximo menos mínimo. |
| Mínimo | Valor más pequeño del conjunto de datos. | Valor mínimo del arreglo. |
| Máximo | Valor más grande del conjunto de datos. | Valor máximo del arreglo. |
| Suma | Total acumulado de todos los valores. | Sumatoria de todos los elementos del arreglo. |
| Recuento | Cantidad total de datos ingresados. | Número de elementos (n) del arreglo de datos. |

> **Nota técnica:** las fórmulas exactas de Curtosis y Asimetría (poblacional vs. muestral, con o sin corrección de sesgo) deben ser confirmadas con el equipo funcional antes del desarrollo, ya que existen variantes de cálculo comúnmente utilizadas en herramientas estadísticas.

---

## 6. Validaciones y manejo de errores

| Escenario | Comportamiento esperado del sistema |
|---|---|
| TextArea vacío al presionar CALCULAR / GENERAR TABLA / GENERAR GRÁFICO | Mostrar un mensaje indicando que debe ingresarse al menos un dato antes de continuar. |
| Datos con caracteres no numéricos (letras, símbolos) | Mostrar un mensaje de error indicando que el arreglo contiene valores no válidos, sin ejecutar el cálculo. |
| Datos con separadores incorrectos (espacios, punto y coma, etc.) | El sistema debe intentar interpretar separadores comunes o, en su defecto, indicar al usuario el formato correcto esperado (valores separados por coma). |
| Conjunto de datos con un único valor (n = 1) | Calcular los estadísticos que sean matemáticamente válidos (media, mínimo, máximo, suma, recuento) e indicar como "No aplica" aquellos que requieran más de un dato (p. ej. varianza, desviación típica, error estándar). |
| No se ha seleccionado tipo de variable | Mostrar un mensaje solicitando seleccionar el tipo de variable antes de generar la tabla de frecuencias. |
| Cantidad insuficiente de datos para agrupar en intervalos (variable continua) | Informar al usuario que se requiere una cantidad mínima de datos para aplicar el método de agrupación en intervalos de clase. |

---

## 7. Requerimientos no funcionales

| ID | Categoría | Descripción |
|---|---|---|
| RNF-01 | Usabilidad | La interfaz debe ser simple e intuitiva, permitiendo a un usuario sin conocimientos técnicos avanzados ingresar datos y obtener resultados en pocos pasos. |
| RNF-02 | Rendimiento | El sistema debe calcular las estadísticas descriptivas, la tabla de frecuencias y el gráfico de caja en un tiempo no mayor a 2 segundos para arreglos de hasta 1000 datos. |
| RNF-03 | Precisión numérica | Los resultados numéricos deben presentarse con una cantidad de decimales configurable (por defecto 2 a 4 decimales), evitando errores de redondeo perceptibles. |
| RNF-04 | Compatibilidad | El sistema debe funcionar correctamente en los navegadores web más utilizados (Chrome, Edge, Firefox) en sus versiones vigentes. |
| RNF-05 | Diseño adaptable (responsive) | Las tres pantallas deben visualizarse correctamente tanto en dispositivos de escritorio como en tablets/móviles. |
| RNF-06 | Disponibilidad de datos entre pantallas | El estado (datos y tipo de variable) debe mantenerse en memoria durante la sesión del usuario mientras navega entre las tres pantallas. |
| RNF-07 | Mantenibilidad | El cálculo de cada estadístico y el método de agrupación de intervalos deben implementarse como funciones independientes y reutilizables, para facilitar su prueba y mantenimiento. |
| RNF-08 | Accesibilidad | Los controles (selector, botones, TextArea) deben contar con etiquetas descriptivas y contraste adecuado, siguiendo buenas prácticas básicas de accesibilidad web. |

---

## 8. Flujo de navegación entre pantallas

- El usuario inicia en la Pantalla 1 (Principal).
- El usuario selecciona el tipo de variable (Continua o Discreta) e ingresa el arreglo de datos en el TextArea.
- **Opción A:** el usuario presiona CALCULAR → el sistema valida los datos → si son válidos, se completa el cuadro de estadísticas en la misma pantalla.
- **Opción B:** el usuario presiona GENERAR TABLA DE FRECUENCIAS → el sistema valida los datos y el tipo de variable → si son válidos, navega a la Pantalla 2 y muestra la tabla correspondiente.
- **Opción C:** el usuario presiona GENERAR GRÁFICO DE CAJA O BIGOTE → el sistema valida los datos → si son válidos, navega a la Pantalla 3 y muestra el diagrama de caja.
- Desde la Pantalla 2 o la Pantalla 3, el usuario puede regresar a la Pantalla 1 para modificar los datos o el tipo de variable y repetir el proceso.

---

## 9. Criterios de aceptación generales

- Dado un arreglo de datos válido, al presionar CALCULAR se deben mostrar correctamente los 15 estadísticos descritos en la sección 5, con valores matemáticamente correctos.
- Dado un arreglo de datos válido y variable Discreta, al presionar GENERAR TABLA DE FRECUENCIAS se debe mostrar una tabla con columnas xi, fi, Fi, hi, Hi, cuyas frecuencias absolutas sumen el total de datos (n) y cuya frecuencia relativa acumulada final sea igual a 1 (100%).
- Dado un arreglo de datos válido y variable Continua, al presionar GENERAR TABLA DE FRECUENCIAS se debe mostrar una tabla agrupada en intervalos de clase consecutivos y sin traslapes, cuyas frecuencias absolutas sumen el total de datos (n).
- Dado un arreglo de datos válido, al presionar GENERAR GRÁFICO DE CAJA O BIGOTE se debe visualizar un diagrama de caja cuyos valores de mínimo, Q1, mediana, Q3 y máximo coincidan con los calculados en el cuadro de estadísticas.
- Ante datos inválidos o vacíos, el sistema debe impedir el cálculo y mostrar un mensaje de error claro, en cualquiera de las tres acciones (CALCULAR, GENERAR TABLA, GENERAR GRÁFICO).
