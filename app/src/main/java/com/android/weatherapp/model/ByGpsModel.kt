package com.android.weatherapp.model

import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 Syed. All rights reserved.
 */

data class ByGpsModel(
        @SerializedName("cod") @Expose private var status: Int = 0,
        @SerializedName("cnt") @Expose private var cnt: Int = 0,
        @SerializedName("message") @Expose private var message: String? = null,
        @SerializedName("list") @Expose private var foreCastList: ArrayList<ForeCastItem>? = null,
        @SerializedName("city") @Expose private var city: City? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.createTypedArrayList(ForeCastItem),
            parcel.readParcelable(City::class.java.classLoader))

    fun getStatus(): Int {
        return status
    }


    fun getCity(): City? {
        return city
    }

    fun getForeCastList(): ArrayList<ForeCastItem>? {
        return foreCastList
    }


    fun getCnt(): Int {
        return cnt
    }

    fun getMessage(): String? {
        return message
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status)
        parcel.writeInt(cnt)
        parcel.writeString(message)
        parcel.writeTypedList(foreCastList)
        parcel.writeParcelable(city, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ByGpsModel> {
        override fun createFromParcel(parcel: Parcel): ByGpsModel {
            return ByGpsModel(parcel)
        }

        override fun newArray(size: Int): Array<ByGpsModel?> {
            return arrayOfNulls(size)
        }
    }
}


class Clouds {

    @SerializedName("all")
    @Expose
    private var all: Int = 0

    fun getAll(): Int? {
        return all
    }
}

