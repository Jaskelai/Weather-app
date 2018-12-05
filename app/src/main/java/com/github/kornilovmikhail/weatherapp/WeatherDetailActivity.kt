package com.github.kornilovmikhail.weatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.kornilovmikhail.weatherapp.entities.City
import kotlinx.android.synthetic.main.activity_weather_detail.*

class WeatherDetailActivity : AppCompatActivity() {
    private var position: Int = 0
    private lateinit var cities: ArrayList<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        cities = CityRepository.getCities()
        position = intent.getIntExtra("position", 0)
        tv_details_city_name.text = cities[position].name
        tv_details_temperature.text = cities[position].currentTemp.currentTemp.toString()
        tv_details_humidity.text = cities[position].currentTemp.humidity.toString()
        tv_details_pressure.text = cities[position].currentTemp.pressure.toString()
        val direction: String = when (cities[position].wind.deg) {
            in 0..22 -> "N"
            in 23..67 -> "NE"
            in 68..112 -> "E"
            in 113..157 -> "SE"
            in 158..202 -> "S"
            in 203..247 -> "SW"
            in 248..292 -> "W"
            in 293..337 -> "NW"
            in 338..359 -> "N"
            else -> "N/A"
        }
        tv_details_wind.text = direction
    }

}
