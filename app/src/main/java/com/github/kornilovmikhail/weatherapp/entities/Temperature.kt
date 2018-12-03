package com.github.kornilovmikhail.weatherapp.entities

import com.google.gson.annotations.SerializedName

data class Temperature(@SerializedName("temp") val currentTemp: Double,
                       @SerializedName("pressure") val pressure: Int,
                       @SerializedName("humidity") val humidity: Int)
