package com.android.weatherapp.repo

import androidx.lifecycle.MutableLiveData
import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.model.ByGpsModel
import com.android.weatherapp.network.APIService
import com.android.weatherapp.utils.Result
import com.android.weatherapp.utils.safeApiCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

class WeatherRepository(private val weatherAPI: APIService) {


    fun getWeather(source: String, key: String): MutableLiveData<ByCityModel> {
        val weatherData = MutableLiveData<ByCityModel>()
        weatherAPI.getWeather(source, key).enqueue(object : Callback<ByCityModel> {
            override fun onResponse(call: Call<ByCityModel>,
                                    response: Response<ByCityModel>) {
                if (response.isSuccessful) {
                    weatherData.value = response.body()
                    println("onResponse " + response.body())
                }
            }

            override fun onFailure(call: Call<ByCityModel>, t: Throwable) {
                weatherData.value = null
                println("onFailure " + t.localizedMessage)
            }
        })
        return weatherData
    }


    suspend fun getGpsWeatherData(lat: String, lng: String, key: String): Result<ByGpsModel> {
        val result = getWeatherByGps(lat, lng, key)
        if (result is Result.Success) {
            return Result.Success(result.data)
        }

        return Result.Error((result as Result.Error).exception)
    }


    private suspend fun getWeatherByGps(lat: String, lng: String, key: String) = safeApiCall(
            call = { fetchWeatherGpsData(lat, lng, key) },
            errorMessage = "Error in fetching data from Remote"
    )

    private suspend fun fetchWeatherGpsData(lat: String, lng: String, key: String): Result<ByGpsModel> {
        val response = weatherAPI.getWeatherByLatLngs(lat, lng, key)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(body)
            }
        }
        return Result.Error(IOException("Error in Fetching data from Remote ${response.code()} ${response.message()}"))
    }


    suspend fun getCityWiseWeather(city: String, key: String): Result<ByCityModel> {
        val result = getWeatherByCity(city, key)
        if (result is Result.Success) {
            return Result.Success(result.data)
        }
        return Result.Error((result as Result.Error).exception)
    }


    private suspend fun getWeatherByCity(city: String, key: String) = safeApiCall(
            call = { fetchWeatherCityData(city, key) },
            errorMessage = "Error in fetching data from Remote"
    )

    private suspend fun fetchWeatherCityData(city: String, key: String): Result<ByCityModel> {
        val response = weatherAPI.getWeatherByCities(city, key)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(body)
            }
        }
        return Result.Error(IOException("Error in Fetching data from Remote ${response.code()} ${response.message()}"))
    }
}