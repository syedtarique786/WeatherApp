package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


data class City(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("coord") val coordinate: Coordinate?,
        @SerializedName("country") val country: String?,
        @SerializedName("timezone") val timezone: Double,
        @SerializedName("sunrise") val sunrise: Double,
        @SerializedName("sunset") val sunset: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readParcelable(Coordinate::class.java!!.classLoader),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeParcelable(coordinate, flags)
        parcel.writeString(country)
        parcel.writeDouble(timezone)
        parcel.writeDouble(sunrise)
        parcel.writeDouble(sunset)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }

    fun getTimeZone(): String {
        val getTimeZoneLong = SimpleDateFormat("zzzz", Locale.US)
        //println("Timezone_String getTimeZoneLong.format(timezone)")
        return getTimeZoneLong.format(timezone)
    }



}