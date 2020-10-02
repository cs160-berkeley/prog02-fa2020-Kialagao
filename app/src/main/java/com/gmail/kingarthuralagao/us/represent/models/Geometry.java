package com.gmail.kingarthuralagao.us.represent.models;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry implements Serializable, Parcelable
{

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("location_type")
    @Expose
    private String locationType;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;
    public final static Parcelable.Creator<Geometry> CREATOR = new Creator<Geometry>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        public Geometry[] newArray(int size) {
            return (new Geometry[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6034936406153661919L;

    protected Geometry(Parcel in) {
        this.location = ((Location) in.readValue((Location.class.getClassLoader())));
        this.locationType = ((String) in.readValue((String.class.getClassLoader())));
        this.viewport = ((Viewport) in.readValue((Viewport.class.getClassLoader())));
    }

    public Geometry() {
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(location);
        dest.writeValue(locationType);
        dest.writeValue(viewport);
    }

    public int describeContents() {
        return 0;
    }

}