package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 Syed. All rights reserved.
 */

data class ForeCastItem(
        @SerializedName("dt") @Expose private var dt: Long? = null,
        @SerializedName("weather") @Expose private var weather: List<Weather>? = null,
        @SerializedName("main") @Expose private var main: Main? = null,
        @SerializedName("clouds") @Expose private var clouds: Cloud? = null,
        @SerializedName("wind") @Expose private var wind: Wind? = null,
        @SerializedName("cod") @Expose private var status: Int = 0,
        @SerializedName("coord") @Expose private var coordinates: Coordinate? = null,
        @SerializedName("dt_txt") @Expose private var dateTime: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.createTypedArrayList(Weather),
            parcel.readParcelable(Main::class.java.classLoader),
            parcel.readParcelable(Cloud::class.java.classLoader),
            parcel.readParcelable(Wind::class.java.classLoader),
            parcel.readInt(),
            parcel.readParcelable(Coordinate::class.java.classLoader),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(dt)
        parcel.writeTypedList(weather)
        parcel.writeParcelable(main, flags)
        parcel.writeParcelable(clouds, flags)
        parcel.writeParcelable(wind, flags)
        parcel.writeInt(status)
        parcel.writeParcelable(coordinates, flags)
        parcel.writeString(dateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForeCastItem> {
        override fun createFromParcel(parcel: Parcel): ForeCastItem {
            return ForeCastItem(parcel)
        }

        override fun newArray(size: Int): Array<ForeCastItem?> {
            return arrayOfNulls(size)
        }
    }

    fun getWeather(): List<Weather>? {
        return weather
    }

    fun getMain(): Main? {
        return main
    }

    fun getClouds(): Cloud? {
        return clouds
    }

    fun getWind(): Wind? {
        return wind
    }

    fun getCoordinate(): Coordinate? {
        return coordinates
    }

    fun getDateTime(): String? {
        return dateTime
    }

    fun getDT(): String? {
        return try {
            SimpleDateFormat("EEEE, MMM yy hh:mm a", Locale.ENGLISH).format(Date(dt!! * 1000))
        } catch (ex: Exception) {
            when(ex) {
                is NumberFormatException, is IndexOutOfBoundsException, is IllegalArgumentException-> {
                    ""
                }
                else -> throw ex
            }
        }
    }

}


/*class Coordinates {

    @SerializedName("lon")
    @Expose
    private var longitude: Float = 0.0f

    @SerializedName("lat")
    @Expose
    private var latitude: Float = 0.0f

    fun getLongitude(): Float {
        return longitude
    }

    fun getLatitude(): Float {
        return latitude
    }

}*/
