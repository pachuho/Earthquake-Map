package com.pachuho.earthquakemap.ui.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private fun getTimeFormatter() = DateTimeFormatter
    .ofPattern("yyyyMMddHHmmss")
    .withZone(ZoneId.of("UTC"))

fun getCurrentTimeAsLong(): Long = LocalDateTime.now().format(getTimeFormatter()).toLong()

fun Long.toLocalDateTimeAsString(): String {
    return this.toLocalDateTime().format(getTimeFormatter())
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.toString().padEnd(14, '0'), getTimeFormatter())
}

fun LocalDateTime.toLong(): Long {
    return this.format(getTimeFormatter()).toLong()
}

fun Long.toMillis(): Long {
    val longAsString = this.toString().padEnd(14, '0')
    val localDateTime = LocalDateTime.parse(longAsString, getTimeFormatter())
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"))
    return zonedDateTime.toInstant().toEpochMilli()
}

fun Long.toLocalDateTimeAsLong(): Long {
    return Instant.ofEpochMilli(this).let {
        getTimeFormatter().format(it).toLong()
    }
}