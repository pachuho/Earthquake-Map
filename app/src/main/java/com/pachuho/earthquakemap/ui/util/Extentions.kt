package com.pachuho.earthquakemap.ui.util

import android.content.Context
import android.widget.Toast
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

private fun getTimeFormatter() = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

fun Long.formatDateTime(): String {
    val dateTime = LocalDateTime.parse(this.toString().padEnd(14, '0'), getTimeFormatter())

    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return dateTime.format(outputFormatter)
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.toString().padEnd(14, '0'), getTimeFormatter())
}

fun Long.toMillis(): Long {
    val longAsString = this.toString().padEnd(14, '0')
    val localDateTime = LocalDateTime.parse(longAsString, getTimeFormatter())
    return localDateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}