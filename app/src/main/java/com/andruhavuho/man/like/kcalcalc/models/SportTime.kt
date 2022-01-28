package com.andruhavuho.man.like.kcalcalc.models

import android.os.Parcel
import android.os.Parcelable
import com.andruhavuho.man.like.kcalcalc.ui.PickedSportsAdapter

data class SportTime(
    val sport: Sport,
    val numHours: Int,
    val numMinutes: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        Sport(
            parcel.readLong(),
            parcel.readString() ?: "Ошибка",
            parcel.readInt()
        ),
        parcel.readInt(),
        parcel.readInt()
    )

    fun getTotalEnergy(): Int {
        val timeHours = numHours + numMinutes.toDouble() / PickedSportsAdapter.NUM_MINUTES_ONE_HOUR
        return (sport.kcalPerHour * timeHours).toInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(sport.id)
        parcel.writeString(sport.name)
        parcel.writeInt(sport.kcalPerHour)
        parcel.writeInt(numHours)
        parcel.writeInt(numMinutes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SportTime> {
        override fun createFromParcel(parcel: Parcel): SportTime {
            return SportTime(parcel)
        }

        override fun newArray(size: Int): Array<SportTime?> {
            return arrayOfNulls(size)
        }
    }
}
