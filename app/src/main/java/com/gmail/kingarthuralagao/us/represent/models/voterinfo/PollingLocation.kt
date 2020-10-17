package com.gmail.kingarthuralagao.us.represent.models.voterinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PollingLocation {
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
    var sources: List<Source>? = null
}