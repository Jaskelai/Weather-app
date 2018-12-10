package com.github.kornilovmikhail.weatherapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.github.kornilovmikhail.weatherapp.db.dao.WeatherDao
import com.github.kornilovmikhail.weatherapp.db.models.City
import com.github.kornilovmikhail.weatherapp.db.models.Temperature
import com.github.kornilovmikhail.weatherapp.db.models.Wind

@Database(entities = [City::class, Temperature::class, Wind::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        private const val DATABASE_NAME = "weather_app.db"
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        WeatherDatabase::class.java, DATABASE_NAME)
                        .build()
            }
            return instance as WeatherDatabase
        }
    }
}