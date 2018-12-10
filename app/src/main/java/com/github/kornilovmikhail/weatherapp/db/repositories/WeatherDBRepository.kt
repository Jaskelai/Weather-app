package com.github.kornilovmikhail.weatherapp.db.repositories

import android.annotation.SuppressLint
import com.github.kornilovmikhail.weatherapp.db.WeatherDatabase
import com.github.kornilovmikhail.weatherapp.db.models.City
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherDBRepository(private val database: WeatherDatabase) {

    fun setCities(cities: List<City>) {
        Completable.fromAction {
            database.weatherDao().insertCities(cities)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        WeatherMemoryRepository.setCities(cities)
    }

    fun deleteCities() {
        Completable.fromAction {
            database.weatherDao().deleteCities()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    @SuppressLint("CheckResult")
    fun getCities(): Single<List<City>> =
            database.weatherDao().getCities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
