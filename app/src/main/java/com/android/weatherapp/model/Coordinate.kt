package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Coordinate(
        @SerializedName("lon") val longitude: Float,
        @SerializedName("lat") val latitude: Float) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(longitude)
        parcel.writeFloat(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coordinate> {
        override fun createFromParcel(parcel: Parcel): Coordinate {
            return Coordinate(parcel)
        }

        override fun newArray(size: Int): Array<Coordinate?> {
            return arrayOfNulls(size)
        }
    }


}