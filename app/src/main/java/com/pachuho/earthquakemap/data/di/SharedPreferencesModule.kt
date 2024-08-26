package com.pachuho.earthquakemap.data.di

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    @Singleton
    @Provides
    @Named(NAME_DATA)
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME_DATA, Context.MODE_PRIVATE)
    }

    companion object {
        const val NAME_DATA = "data"
    }
}

inline fun <reified T> SharedPreferences.get(key: String): T? {
    val json = getString(key, null)
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi.adapter(T::class.java)

    return json?.let {
        adapter.fromJson(it)
    }
}

inline fun <reified T> SharedPreferences.set(key: String, value: T) {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi.adapter(T::class.java)
    val json = adapter.toJson(value)
    edit().putString(key, json).apply()
}
