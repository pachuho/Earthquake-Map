package com.pachuho.earthquakemap.data.di

import android.content.Context
import androidx.room.Room
import com.pachuho.earthquakemap.data.db.AppDatabase
import com.pachuho.earthquakemap.data.db.EarthquakeDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "Earthquake.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideEarthquakeDao(appDatabase: AppDatabase): EarthquakeDao = appDatabase.earthquakeDao()
}