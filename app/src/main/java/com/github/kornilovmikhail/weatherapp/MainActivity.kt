package com.github.kornilovmikhail.weatherapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.support.v7.widget.DividerItemDecoration
import com.github.kornilovmikhail.weatherapp.entities.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), ListCallback {

    private val code = 147
    private lateinit var lm: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_cities.layoutManager = LinearLayoutManager(this)
        rv_cities.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionRationale()
        } else {
            getCities()
        }
    }

    private fun requestPermissionRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            val message = "GPS permissions is needed to show nearest cities"
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
        if (allowed) getCities() else showPermsOnSetting()
    }

    private fun showPermsOnSetting() {
        Snackbar.make(findViewById(R.id.activity_main), "Permissions not granted", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS") { openApplicationSettings() }
                .show()
    }

    private fun openApplicationSettings() {
        val appSettingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + this.packageName))
        startActivityForResult(appSettingIntent, code)
    }

    private fun getCities() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            val mCompositeDisposable = CompositeDisposable()
            val location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            val longitude = location.longitude
            val latitude = location.latitude
            val client = WeatherService.service()
            mCompositeDisposable.add(client.loadCities(latitude, longitude, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        run {
                            CityRepository.setCities(response.listCities as ArrayList<City>)
                            rv_cities.adapter = CityAdapter(response.listCities)
                        }
                    }, {

                    }))
        }
    }

    override fun callback(position: Int) {
        val intent = Intent(this@MainActivity, WeatherDetailActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}
