package com.github.kornilovmikhail.weatherapp

import com.github.kornilovmikhail.weatherapp.db.models.ListCities
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("find")
    fun loadCities(@Query("lat") lat: Double?, @Query("lon") lon: Double?,
                   @Query("cnt") cnt: Int?): Single<ListCities>
}
