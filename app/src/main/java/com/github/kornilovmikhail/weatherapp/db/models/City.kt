package com.github.kornilovmikhail.weatherapp.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cities")
data class City(@ColumnInfo(index = true) @PrimaryKey(autoGenerate = true) var id: Int,
                @SerializedName("name") val name: String,
                @SerializedName("main") @Embedded(prefix = "weather") val currentTemp: Temperature,
                @SerializedName("wind") @Embedded(prefix = "wind") val wind: Wind)
