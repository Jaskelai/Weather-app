package com.github.kornilovmikhail.weatherapp.db.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "temperature")
data class Temperature(@PrimaryKey(autoGenerate = true) var id: Int,
                       @SerializedName("temp") val currentTemp: Double,
                       @SerializedName("pressure") val pressure: Int,
                       @SerializedName("humidity") val humidity: Int)
