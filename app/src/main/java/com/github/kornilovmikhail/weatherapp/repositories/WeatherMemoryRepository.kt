package com.github.kornilovmikhail.weatherapp.repositories

import com.github.kornilovmikhail.weatherapp.db.models.City

class WeatherMemoryRepository {
    companion object {
        private var cities: List<City> = ArrayList()

        fun getCities(): List<City> {
            return cities
        }

        fun setCities(cities: List<City>) {
            Companion.cities = cities
        }
    }
}
