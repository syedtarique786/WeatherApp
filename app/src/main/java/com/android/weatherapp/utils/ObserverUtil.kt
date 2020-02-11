package com.android.weatherapp.utils

/**
 * Created by    :   Syed
 * Created on    :   04,February,2020.
 * Modified By   :
 * Modified On   :   04,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> LiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}