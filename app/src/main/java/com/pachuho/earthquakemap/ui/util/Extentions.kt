package com.pachuho.earthquakemap.ui.util

import android.content.Context
import android.widget.Toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.formatDateTime(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val dateTime = LocalDateTime.parse(this, inputFormatter)

    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return dateTime.format(outputFormatter)
}