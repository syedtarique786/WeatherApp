package com.android.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 Syed. All rights reserved.
 */

class ByCityModel {

    @SerializedName("cod")
    @Expose
    private var status: Int = 0

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("coord")
    @Expose
    private var coordinates: Coordinate? = null

    @SerializedName("weather")
    @Expose
    private var weather: List<Weather>? = null

    @SerializedName("base")
    @Expose
    private var base: String? = null


    @SerializedName("main")
    @Expose
    private var main: Main? = null


    @SerializedName("visibility")
    @Expose
    private var visibility: Int? = null


    @SerializedName("wind")
    @Expose
    private var wind: Wind? = null


    @SerializedName("clouds")
    @Expose
    private var clouds: Cloud? = null


    @SerializedName("dt")
    @Expose
    private var dt: Long? = null


    @SerializedName("sys")
    @Expose
    private var sys: Sys? = null

    @SerializedName("timezone")
    @Expose
    private var timezone: Int? = 0

    @SerializedName("id")
    @Expose
    private var id: Int? = 0

    @SerializedName("name")
    @Expose
    private var cityName: String? = ""


    fun getStatus(): Int {
        return status
    }

    fun getBase(): String? {
        return base
    }

    fun getWeather(): List<Weather>? {
        return weather
    }

    fun getCoordinate(): Coordinate? {
        return coordinates
    }

    fun getMain(): Main? {
        return main
    }

    fun getVisibility(): Int? {
        return visibility
    }

    fun getWind(): Wind? {
        return wind
    }

    fun getClouds(): Cloud? {
        return clouds
    }

    fun getDt(): String {
        return try {
            SimpleDateFormat("EEE, MMM dd, hh:mm a", Locale.ENGLISH).format(Date(dt!! * 1000))
        } catch (ex: Exception) {
            when(ex) {
                is java.lang.NumberFormatException, is IndexOutOfBoundsException, is IllegalArgumentException -> {
                    ""
                }
                else -> throw ex
            }
        }
    }

    fun getSys(): Sys? {
        return sys
    }

    fun getTimezoneDt(): Int? {
        return timezone
    }

    fun getId(): Int? {
        return id
    }

    fun getCityName(): String? {
        return cityName
    }

    fun getMessage(): String? {
        return message
    }

}

class Sys {
    /*
    "type": 1,
    "id": 9176,
    "country": "IN",
    "sunrise": 1580692867,
    "sunset": 1580732334
    * */

    @SerializedName("country")
    @Expose
    private var country: String? = ""

    @SerializedName("type")
    @Expose
    private var type: Int = 0

    @SerializedName("id")
    @Expose
    private var id: Int = 0

    @SerializedName("sunrise")
    @Expose
    private var sunrise: Int = 0

    @SerializedName("sunset")
    @Expose
    private var sunset: Int = 0

    fun getCountry(): String? {
        return country
    }

    fun getSunrise(): Int? {
        return sunrise
    }

    fun getSunset(): Int? {
        return sunset
    }

    fun getType(): Int? {
        return type
    }

    fun getId(): Int? {
        return id
    }

}