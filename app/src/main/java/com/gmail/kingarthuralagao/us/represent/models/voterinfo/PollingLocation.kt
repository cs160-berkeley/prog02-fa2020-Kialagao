package com.gmail.kingarthuralagao.us.represent.models.voterinfo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PollingLocation : Serializable, Parcelable {
    @SerializedName("address")
    @Expose
    var address: Address? = null

    @SerializedName("notes")
    @Expose
    var notes: String? = null

    @SerializedName("pollingHours")
    @Expose
    var pollingHours: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null

    @SerializedName("sources")
    @Expose
    var sources: List<Source?>? = null

    protected constructor(`in`: Parcel) {
        address = `in`.readValue(Address::class.java.classLoader) as Address?
        notes = `in`.readValue(String::class.java.classLoader) as String?
        pollingHours = `in`.readValue(String::class.java.classLoader) as String?
        latitude = `in`.readValue(Double::class.java.classLoader) as Double?
        longitude = `in`.readValue(Double::class.java.classLoader) as Double?
        `in`.readList(sources!!, Source::class.java.getClassLoader())
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(address)
        dest.writeValue(notes)
        dest.writeValue(pollingHours)
        dest.writeValue(latitude)
        dest.writeValue(longitude)
        dest.writeList(sources)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<PollingLocation?> =
            object : Parcelable.Creator<PollingLocation?> {
                override fun createFromParcel(`in`: Parcel): PollingLocation? {
                    return PollingLocation(`in`)
                }

                override fun newArray(size: Int): Array<PollingLocation?> {
                    return arrayOfNulls(size)
                }
            }
        private const val serialVersionUID = 6034343489911130262L
    }
}
