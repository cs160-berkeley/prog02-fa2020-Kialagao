package com.gmail.kingarthuralagao.us.represent.models;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results implements Serializable, Parcelable
{

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    public final static Parcelable.Creator<Results> CREATOR = new Creator<Results>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        public Results[] newArray(int size) {
            return (new Results[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5501885994971779204L;

    protected Results(Parcel in) {
        in.readList(this.results, (com.gmail.kingarthuralagao.us.represent.models.Result.class.getClassLoader()));
    }

    public Results() {
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}