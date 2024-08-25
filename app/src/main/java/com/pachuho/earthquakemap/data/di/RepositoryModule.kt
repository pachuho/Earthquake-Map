package com.pachuho.earthquakemap.data.di

import com.pachuho.earthquakemap.data.repository.EarthquakeRepository
import com.pachuho.earthquakemap.data.repository.EarthquakeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideEarthquakeRepository(earthquakeRepositoryImpl: EarthquakeRepositoryImpl): EarthquakeRepository
}