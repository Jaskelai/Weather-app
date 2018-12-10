package com.github.kornilovmikhail.weatherapp.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.kornilovmikhail.weatherapp.db.models.City
import io.reactivex.Single

@Dao
interface WeatherDao {
    @Insert
    fun insertCities(cities: List<City>)

    @Query("SELECT * FROM cities")
    fun getCities(): Single<List<City>>

    @Query("DELETE FROM cities")
    fun deleteCities()

}