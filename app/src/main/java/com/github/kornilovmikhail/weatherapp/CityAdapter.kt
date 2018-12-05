package com.github.kornilovmikhail.weatherapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kornilovmikhail.weatherapp.entities.City
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cities_list_item.view.*

class CityAdapter(items: ArrayList<City>)
    : RecyclerView.Adapter<CityAdapter.CityHolder>() {

    private var cities: ArrayList<City> = items
    private lateinit var listCallback: ListCallback

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CityHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cities_list_item, parent, false)
        listCallback = view.context as ListCallback
        return CityHolder(view)
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(cities[position].name, cities[position].currentTemp.currentTemp.toString())
        holder.itemView.setOnClickListener { listCallback.callback(position) }
    }

    class CityHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        fun bind(cityName: String, curTemp: String) {
            containerView.tv_list_city_name.text = cityName
            containerView.tv_list_temperature.text = curTemp
        }
    }

}
