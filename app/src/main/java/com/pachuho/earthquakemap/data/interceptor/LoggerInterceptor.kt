package com.pachuho.earthquakemap.data.interceptor

import okhttp3.logging.HttpLoggingInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggerInterceptor @Inject constructor() : HttpLoggingInterceptor.Logger {
    companion object {
        const val LOG_DIVIDER = "================================================================"
    }

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter: JsonAdapter<Any> = moshi.adapter(Any::class.java)

    override fun log(message: String) {
        val trimmedMessage = message.trim { it <= ' ' }
        if (trimmedMessage.startsWith("{") && trimmedMessage.endsWith("}")
            || trimmedMessage.startsWith("[") && trimmedMessage.endsWith("]")
        ) {
            try {
                val json = moshi.adapter(Any::class.java).fromJson(trimmedMessage)
                val prettyPrintJson = jsonAdapter.indent("  ").toJson(json)
                Log.w("Network Response", LOG_DIVIDER + "\n" + prettyPrintJson + "\n" + LOG_DIVIDER)
            } catch (e: Exception) {
                Log.w("Network Response", message, e)
            }
        } else {
            Log.w("Network Response", message)
        }
    }
}