package com.pachuho.earthquakemap.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pachuho.earthquakemap.data.model.Earthquake


@Database(entities = [Earthquake::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun earthquakeDao(): EarthquakeDao
}