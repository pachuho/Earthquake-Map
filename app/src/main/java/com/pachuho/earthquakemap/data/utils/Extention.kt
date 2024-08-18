package com.pachuho.earthquakemap.data.utils

import retrofit2.Response

fun<T> getException(response: Response<T>) =
    "code: ${response.code()}\nmessage: ${response.raw()}"