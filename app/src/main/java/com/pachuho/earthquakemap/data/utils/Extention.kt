package com.pachuho.earthquakemap.data.utils

import retrofit2.Response
import java.time.LocalDateTime

fun<T> getException(response: Response<T>) =
    "code: ${response.code()}\nmessage: ${response.raw()}"

fun Int.isToday(): Boolean {
    LocalDateTime.now().run {
        val now = (year.toString() + monthValue.toString().padStart(2, '0') + dayOfMonth.toString().padStart(2, '0')).toInt()
        return this@isToday == now
    }
}