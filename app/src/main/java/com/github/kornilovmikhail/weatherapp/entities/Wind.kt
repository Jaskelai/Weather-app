package com.github.kornilovmikhail.weatherapp.entities

import com.google.gson.annotations.SerializedName

data class Wind(@SerializedName("deg") val deg: Int)
