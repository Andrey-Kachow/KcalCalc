package com.andruhavuho.man.like.kcalcalc.models

import android.os.Parcel
import android.os.Parcelable

data class Sport(
    val id: Long,
    val name: String,
    val kcalPerHour: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "Ошибка",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(kcalPerHour)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sport> {
        override fun createFromParcel(parcel: Parcel): Sport {
            return Sport(parcel)
        }

        override fun newArray(size: Int): Array<Sport?> {
            return arrayOfNulls(size)
        }
    }
}
