package com.android.weatherapp.utils

/**
 * Created by    :   Syed
 * Created on    :   08,February,2020.
 * Modified By   :
 * Modified On   :   08,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}