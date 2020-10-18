package com.gmail.kingarthuralagao.us.represent.models.voterinfo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VoterInfoResult() : Parcelable {
    @SerializedName("election")
    @Expose(serialize = false)
    var election: Election? = null

    @SerializedName("normalizedInput")
    @Expose(serialize = false)
    var normalizedInput: NormalizedInput? = null

    @SerializedName("pollingLocations")
    @Expose
    var pollingLocations: List<PollingLocation>? = null

    @SerializedName("earlyVoteSites")
    @Expose(serialize = false)
    var earlyVoteSites: List<EarlyVoteSite>? = null

    @SerializedName("dropOffLocations")
    @Expose
    var dropOffLocations: List<DropOffLocation>? = null

    @SerializedName("state")
    @Expose(serialize = false)
    var state: List<State>? = null

    @SerializedName("kind")
    @Expose(serialize = false)
    var kind: String? = null

    @SerializedName("contests")
    @Expose(serialize = false)
    var contest: Contests? = null

    @SerializedName("mailOnly")
    @Expose(serialize = false)
    var mailOnly: Boolean = false

    constructor(parcel: Parcel) : this() {
        kind = parcel.readString()
        mailOnly = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeByte(if (mailOnly) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VoterInfoResult> {
        override fun createFromParcel(parcel: Parcel): VoterInfoResult {
            return VoterInfoResult(parcel)
        }

        override fun newArray(size: Int): Array<VoterInfoResult?> {
            return arrayOfNulls(size)
        }
    }
}
