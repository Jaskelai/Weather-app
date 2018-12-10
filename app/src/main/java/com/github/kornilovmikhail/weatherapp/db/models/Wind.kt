package com.github.kornilovmikhail.weatherapp.db.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "winds")
data class Wind(@PrimaryKey(autoGenerate = true) var id: Int,
                @SerializedName("deg") val degree: Int,
                @SerializedName("speed") val speed: Double)
