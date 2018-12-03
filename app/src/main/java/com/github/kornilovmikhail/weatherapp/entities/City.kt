package com.github.kornilovmikhail.weatherapp.entities

import com.google.gson.annotations.SerializedName

data class City(@SerializedName("name") val name: String,
                @SerializedName("main") val currentTemp: Temperature,
                @SerializedName("wind") val wind: Wind)
