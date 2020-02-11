package com.android.weatherapp.view

import android.view.View
import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.model.ForeCastItem

/**
 * Created by    :   Syed
 * Created on    :   06,February,2020.
 * Modified By   :
 * Modified On   :   06,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

interface RecyclerViewClickListener {
    fun onForcastWaetherItemClick(view: View, foreCastItem: ForeCastItem)

    fun onCityWaetherItemClick(view: View, foreCastItem: ByCityModel)
}