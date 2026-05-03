package com.echohabit.app.core.utils

import kotlin.math.round

fun Float.roundToTwoDecimals(): Float = round(this * 100) / 100

fun Long.toMinutes(): Long = this / 1000 / 60

fun Long.toSeconds(): Long = this / 1000

fun Int.percentageOf(total: Int): Float = if (total > 0) (this.toFloat() / total) * 100 else 0f

fun List<Float>.average(): Float = if (this.isNotEmpty()) this.sum() / this.size else 0f
