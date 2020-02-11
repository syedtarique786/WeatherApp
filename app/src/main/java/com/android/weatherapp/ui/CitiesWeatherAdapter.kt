package com.android.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ItemCityWeatherBinding
import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.view.BindingViewHolder
import com.android.weatherapp.view.RecyclerViewClickListener


/**
 * Created by    :   Syed
 * Created on    :   04,February,2020.
 * Modified By   :
 * Modified On   :   04,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

class CitiesWeatherAdapter(private var clickListener: RecyclerViewClickListener) :
        RecyclerView.Adapter<BindingViewHolder<ItemCityWeatherBinding>>() {

    private var citiesWeatherList: ArrayList<ByCityModel>

    init {
        println("Initialized_city_adapter.........")
        citiesWeatherList = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemCityWeatherBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_city_weather, parent, false))
    }


    override fun getItemCount(): Int {
        return if (citiesWeatherList?.size == 0) 0
        else citiesWeatherList?.size
    }


    override fun onBindViewHolder(holder: BindingViewHolder<ItemCityWeatherBinding>, position: Int) {
        holder.binding.cvItemRoot.setBackgroundResource(R.drawable.splash_gradient)
        holder.binding.pos = position
        holder.binding.cityModel = citiesWeatherList[position]
        holder.binding.executePendingBindings()
        holder.binding.cvItemRoot.setOnClickListener(View.OnClickListener {
            clickListener.onCityWaetherItemClick(it, citiesWeatherList[position])
        })

    }

    fun addItem(value: ByCityModel) {
        citiesWeatherList.add(value)
        notifyDataSetChanged()
    }
}