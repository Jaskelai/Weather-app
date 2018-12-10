package com.github.kornilovmikhail.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import com.github.kornilovmikhail.weatherapp.db.repositories.WeatherDBRepository
import com.github.kornilovmikhail.weatherapp.db.WeatherDatabase
import com.github.kornilovmikhail.weatherapp.db.models.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, ListCallback {

    private val code = 147

    private lateinit var lm: LocationManager
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private var location: Location? = null
    private lateinit var cityRepository: WeatherDBRepository
    private lateinit var database: WeatherDatabase


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = WeatherDatabase.getInstance(this)
        cityRepository = WeatherDBRepository(database)
        rv_cities.layoutManager = LinearLayoutManager(this)
        rv_cities.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionRationale()
        } else {
            cityRepository.getCities()
                    .subscribeBy(onSuccess = {
                        if (!it.isEmpty()) {
                            rv_cities.adapter = CityAdapter(it)
                        } else {
                            location = getLocation()
                            updateInfo(location)
                        }
                    }, onError = {})

        }
        activity_main.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        val location: Location? = getLocation()
        updateInfo(location)
    }

    private fun requestPermissionRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            val message = getString(R.string.perm_reason)
            Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_LONG)
                    .setAction("GRANT") {
                        requestPerms()
                    }
                    .show()
        } else {
            requestPerms()
        }
    }

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissions, code)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var allowed = false
        when (requestCode) {
            code -> {
                allowed = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
            else -> {
            }
        }

        if (allowed) {
            val location = getLocation()
            updateInfo(location)
        } else showPermsOnSetting()
    }

    private fun showPermsOnSetting() {
        Snackbar.make(findViewById(R.id.activity_main), getString(R.string.perm_not_granted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.settings)) { openApplicationSettings() }
                .show()
    }

    private fun openApplicationSettings() {
        val appSettingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + this.packageName))
        startActivityForResult(appSettingIntent, code)
    }

    private fun getLocation(): Location? {
        val cr = Criteria()
        cr.accuracy = Criteria.ACCURACY_FINE
        val providers = lm.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val location = lm.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        return bestLocation
    }

    private fun updateInfo(location: Location?) {
        val longitude = location?.longitude
        val latitude = location?.latitude
        val client = WeatherService.service()
        var cities: List<City> = ArrayList()
        cityRepository.deleteCities()
        mCompositeDisposable.add(client.loadCities(latitude, longitude, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response ->
                    cityRepository.setCities(response.listCities)
                    cities = response.listCities
                }
                .subscribe({
                    rv_cities.adapter = CityAdapter(cities)
                    activity_main.isRefreshing = false
                    Toast.makeText(this, getString(R.string.cities_loaded), Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this, getString(R.string.server_fault), Toast.LENGTH_SHORT).show()
                }))
    }

    override fun callback(position: Int) {
        val intent = Intent(this@MainActivity, WeatherDetailActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
        database.close()
    }
}
