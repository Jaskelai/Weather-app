package com.github.kornilovmikhail.weatherapp.entities

import com.google.gson.annotations.SerializedName

data class ListCities(@SerializedName("list") val listCities: List<City>)
