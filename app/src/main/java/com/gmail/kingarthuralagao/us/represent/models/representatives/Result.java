package com.gmail.kingarthuralagao.us.represent.models.representatives;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("normalizedInput")
    @Expose
    private NormalizedInput normalizedInput;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("divisions")
    @Expose
    private Divisions divisions;
    @SerializedName("offices")
    @Expose
    private List<Office> offices = null;
    @SerializedName("officials")
    @Expose
    private List<Official> officials = null;

    public NormalizedInput getNormalizedInput() {
        return normalizedInput;
    }

    public void setNormalizedInput(NormalizedInput normalizedInput) {
        this.normalizedInput = normalizedInput;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Divisions getDivisions() {
        return divisions;
    }

    public void setDivisions(Divisions divisions) {
        this.divisions = divisions;
    }

    public List<Office> getOffices() {
        return offices;
    }

    public void setOffices(List<Office> offices) {
        this.offices = offices;
    }

    public List<Official> getOfficials() {
        return officials;
    }

    public void setOfficials(List<Official> officials) {
        this.officials = officials;
    }

}
