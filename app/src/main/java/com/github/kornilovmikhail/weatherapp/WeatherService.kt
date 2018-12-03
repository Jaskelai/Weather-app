package com.github.kornilovmikhail.weatherapp

import com.github.kornilovmikhail.weatherapp.interceptors.ApiKeyInterceptor
import com.github.kornilovmikhail.weatherapp.interceptors.MetricInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {

    const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

    fun service(): OpenWeatherApi {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor.create())
                .addInterceptor(MetricInterceptor.create())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(OpenWeatherApi::class.java)
    }
}