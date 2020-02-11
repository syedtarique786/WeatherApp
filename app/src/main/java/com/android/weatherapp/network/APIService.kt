package com.android.weatherapp.network

import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.model.ByGpsModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {


    @GET("weather?units=metric")
    fun getWeatherByCity(@Query("unit") unit: String,
                         @Query("q") cityName: String, @Query("appid") apiKey: String): Call<ByCityModel>

    @GET("weather?units=metric")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey: String): Call<ByCityModel>

    @GET("weather?units=metric")
    suspend fun getWeatherByCities(@Query("q") cityName: String,
                           @Query("appid") apiKey: String): Response<ByCityModel>


    @GET("forecast?units=metric")
    suspend fun getWeatherByLatLngs(@Query("lat") latitude: String,
                           @Query("lon") longitude: String,
                           @Query("appid") apiKey: String): Response<ByGpsModel>

}
