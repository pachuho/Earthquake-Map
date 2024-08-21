package com.pachuho.earthquakemap.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pachuho.earthquakemap.data.model.Earthquake


@Dao
interface EarthquakeDao {
    @Query("SELECT COUNT(*) FROM Earthquake")
    suspend fun getDataCount(): Int

    @Query("SELECT * FROM Earthquake ORDER BY EQ_ID DESC LIMIT 1")
    suspend fun getLatestEarthquake(): Earthquake?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEarthquakes(earthquakes: List<Earthquake>)

    @Query("SELECT * FROM Earthquake")
    suspend fun getEarthquakes(): List<Earthquake>
}