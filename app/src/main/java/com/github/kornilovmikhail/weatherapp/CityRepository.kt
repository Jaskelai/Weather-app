package com.github.kornilovmikhail.weatherapp

import com.github.kornilovmikhail.weatherapp.entities.City

class CityRepository {

    companion object {
        private var cities: ArrayList<City> = ArrayList()

        fun setCities(cities: ArrayList<City>) {
            this.cities = cities
        }

        fun getCities(): ArrayList<City> {
            return cities
        }
    }
}
