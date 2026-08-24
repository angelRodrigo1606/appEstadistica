package com.ngel.appestadistica.ui

import java.text.DecimalFormat

private val numberFormat = DecimalFormat("0.0000")

fun Double.display(): String = numberFormat.format(this)
fun Double?.displayOrNA(): String = this?.display() ?: "No aplica"
fun Int.display(): String = toString()
