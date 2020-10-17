package com.gmail.kingarthuralagao.us.represent.models.voterinfo

class Contests {
}

class CorrespondenceAddress { /*
    @SerializedName("locationName")
    @Expose(serialize = false)
    private String locationName;
    @SerializedName("line1")
    @Expose(serialize = false)
    private String line1;
    @SerializedName("city")
    @Expose(serialize = false)
    private String city;
    @SerializedName("state")
    @Expose(serialize = false)
    private String state;
    @SerializedName("zip")
    @Expose(serialize = false)
    private String zip;*/
}

class EarlyVoteSite {
    /*
    @SerializedName("address")
    @Expose(serialize = false)
    var address: Address? = null

    @SerializedName("notes")
    @Expose(serialize = false)
    var notes: String? = null

    @SerializedName("pollingHours")
    @Expose(serialize = false)
    var pollingHours: String? = null

    @SerializedName("latitude")
    @Expose(serialize = false)
    var latitude: Double? = null

    @SerializedName("longitude")
    @Expose(serialize = false)
    var longitude: Double? = null

    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null

    @SerializedName("voterServices")
    @Expose(serialize = false)
    var voterServices: String? = null

    @SerializedName("startDate")
    @Expose(serialize = false)
    var startDate: String? = null

    @SerializedName("endDate")
    @Expose(serialize = false)
    var endDate: String? = null

    @SerializedName("sources")
    @Expose(serialize = false)
    var sources: List<Source>? = null*/
}

class Election {
    /*
    @SerializedName("id")
    @Expose(serialize = false)
    var id: String? = null

    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null

    @SerializedName("electionDay")
    @Expose(serialize = false)
    var electionDay: String? = null

    @SerializedName("ocdDivisionId")
    @Expose(serialize = false)
    var ocdDivisionId: String? = null*/
}

class ElectionAdministrationBody {
    /*
    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null

    @SerializedName("electionInfoUrl")
    @Expose(serialize = false)
    var electionInfoUrl: String? = null

    @SerializedName("votingLocationFinderUrl")
    @Expose(serialize = false)
    var votingLocationFinderUrl: String? = null

    @SerializedName("ballotInfoUrl")
    @Expose(serialize = false)
    var ballotInfoUrl: String? = null

    @SerializedName("correspondenceAddress")
    @Expose(serialize = false)
    var correspondenceAddress: CorrespondenceAddress? = null*/
}

class LocalJurisdiction {
    /*
    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null

    @SerializedName("electionAdministrationBody")
    @Expose(serialize = false)
    var electionAdministrationBody: ElectionAdministrationBody? = null

    @SerializedName("sources")
    @Expose(serialize = false)
    var sources: List<Source>? = null*/
}

class NormalizedInput {
    /*
    @SerializedName("line1")
    @Expose(serialize = false)
    var line1: String? = null

    @SerializedName("city")
    @Expose(serialize = false)
    var city: String? = null

    @SerializedName("state")
    @Expose(serialize = false)
    var state: String? = null

    @SerializedName("zip")
    @Expose(serialize = false)
    var zip: String? = null*/
}

class PhysicalAddress {
    /*
    @SerializedName("line1")
    @Expose(serialize = false)
    var line1: String? = null

    @SerializedName("line2")
    @Expose(serialize = false)
    var line2: String? = null

    @SerializedName("city")
    @Expose(serialize = false)
    var city: String? = null

    @SerializedName("state")
    @Expose(serialize = false)
    var state: String? = null

    @SerializedName("zip")
    @Expose(serialize = false)
    var zip: String? = null*/
}

class Source {
    /*
    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null

    @SerializedName("official")
    @Expose(serialize = false)
    var official: Boolean? = null*/
}

class State {
    /*
    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null

    @SerializedName("electionAdministrationBody")
    @Expose(serialize = false)
    var electionAdministrationBody: ElectionAdministrationBody? = null

    @SerializedName("local_jurisdiction")
    @Expose(serialize = false)
    var localJurisdiction: LocalJurisdiction? = null

    @SerializedName("sources")
    @Expose(serialize = false)
    var sources: List<Source>? = null*/
}