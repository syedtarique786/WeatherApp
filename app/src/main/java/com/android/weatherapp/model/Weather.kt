package com.android.weatherapp.model

import android.os.Parcel
import android.os.Parcelable

data class Weather(val id: Int,
                   val main: String,
                   val avatar: String,
                   val description: String,
                   val icon: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(main)
        parcel.writeString(avatar)
        parcel.writeString(description)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }

}

/*@SerializedName("id")
    @Expose
    private var id: Int = 0

    @SerializedName("main")
    @Expose
    private var main: String = ""

    @SerializedName("description")
    @Expose
    private var description: String = ""


    @SerializedName("icon")
    @Expose
    private var icon: String = ""*/
