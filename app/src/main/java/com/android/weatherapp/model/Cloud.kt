package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Cloud(
        @SerializedName("all") val all: Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(all)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cloud> {
        override fun createFromParcel(parcel: Parcel): Cloud {
            return Cloud(parcel)
        }

        override fun newArray(size: Int): Array<Cloud?> {
            return arrayOfNulls(size)
        }
    }


}