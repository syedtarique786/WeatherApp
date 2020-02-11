package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Wind(
        @SerializedName("speed") val speed: Float,
        @SerializedName("deg") val degree: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(speed)
        parcel.writeInt(degree)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wind> {
        override fun createFromParcel(parcel: Parcel): Wind {
            return Wind(parcel)
        }

        override fun newArray(size: Int): Array<Wind?> {
            return arrayOfNulls(size)
        }
    }


    fun getSpeed(): Float? {
        return speed
    }

    fun getDegree(): Int? {
        return degree
    }

}