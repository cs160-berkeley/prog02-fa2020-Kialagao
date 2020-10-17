package com.gmail.kingarthuralagao.us.represent.models.voterinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VoterInfoResult {
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
}
