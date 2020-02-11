package com.android.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ItemForecastBinding
import com.android.weatherapp.model.ForeCastItem
import com.android.weatherapp.view.BindingViewHolder
import com.android.weatherapp.view.RecyclerViewClickListener


/**
 * Created by    :   Syed
 * Created on    :   04,February,2020.
 * Modified By   :
 * Modified On   :   04,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

class WeatherForecastAdapter(private var foreCastList: ArrayList<ForeCastItem>,
                             private var clickListener: RecyclerViewClickListener) :
        RecyclerView.Adapter<BindingViewHolder<ItemForecastBinding>>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemForecastBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false))
    }


    override fun getItemCount(): Int {
        return foreCastList.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemForecastBinding>, position: Int) {
        holder.binding.cvItemRoot.setBackgroundResource(R.drawable.splash_gradient)
        holder.binding.position = position
        holder.binding.gpsModel = foreCastList[position]
        holder.binding.executePendingBindings()
        holder.binding.cvItemRoot.setOnClickListener(View.OnClickListener {
            clickListener.onForcastWaetherItemClick(it, foreCastList[position])
        }
        )

    }
}