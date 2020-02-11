package com.android.weatherapp.view

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by    :   Syed
 * Created on    :   04,February,2020.
 * Modified By   :
 * Modified On   :   04,February,2020.
 * Copyright (c) 2020 @Syed . All rights reserved.
 */

class BindingViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding: T

    init {
        binding = DataBindingUtil.bind<ViewDataBinding>(itemView) as T
    }


}