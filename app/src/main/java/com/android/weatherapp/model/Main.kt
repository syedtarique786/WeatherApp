package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Main(
        @SerializedName("temp") val temp: Float,
        @SerializedName("feels_like") val feelsLike: Float,
        @SerializedName("temp_min") val minTemp: Float,
        @SerializedName("temp_max") val maxTemp: Float,
        @SerializedName("pressure") val pressure: Int,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("sea_level") val seaLevel: Int,
        @SerializedName("grnd_level") val groundLevel: Int,
        @SerializedName("temp_kf") val tempKf: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(temp)
        parcel.writeFloat(feelsLike)
        parcel.writeFloat(minTemp)
        parcel.writeFloat(maxTemp)
        parcel.writeInt(pressure)
        parcel.writeInt(humidity)
        parcel.writeInt(seaLevel)
        parcel.writeInt(groundLevel)
        parcel.writeString(tempKf)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Main> {
        override fun createFromParcel(parcel: Parcel): Main {
            return Main(parcel)
        }

        override fun newArray(size: Int): Array<Main?> {
            return arrayOfNulls(size)
        }
    }


    fun getTemp(): Float? {
        return temp
    }

    fun getFeelsLike(): Float? {
        return feelsLike
    }

    fun getMinTemp(): Float? {
        return minTemp
    }

    fun getMaxTemp(): Float? {
        return maxTemp
    }

    fun getPressure(): Int? {
        return pressure
    }

    fun getHumidity(): Int? {
        return humidity
    }

    fun getSeaLevel(): Int? {
        return seaLevel
    }

    fun getGrounLevel(): Int? {
        return groundLevel
    }

}

