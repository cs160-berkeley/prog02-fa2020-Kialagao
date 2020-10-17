package com.gmail.kingarthuralagao.us.represent.models.voterinfo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DropOffLocation {

    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("pollingHours")
    @Expose
    private String pollingHours;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("voterServices")
    @Expose
    private String voterServices;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("sources")
    @Expose
    private List<Source> sources = null;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPollingHours() {
        return pollingHours;
    }

    public void setPollingHours(String pollingHours) {
        this.pollingHours = pollingHours;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoterServices() {
        return voterServices;
    }

    public void setVoterServices(String voterServices) {
        this.voterServices = voterServices;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

}