package com.gmail.kingarthuralagao.us.represent.models;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable, Parcelable
{
    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents = null;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    public final static Parcelable.Creator<Result> CREATOR = new Creator<Result>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return (new Result[size]);
        }
    };
    private final static long serialVersionUID = -2575614696178070255L;

    protected Result(Parcel in) {
        in.readList(this.addressComponents, (com.gmail.kingarthuralagao.us.represent.models.AddressComponent.class.getClassLoader()));
        this.formattedAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.geometry = ((Geometry) in.readValue((Geometry.class.getClassLoader())));
        this.placeId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.types, (java.lang.String.class.getClassLoader()));
    }

    public Result() {
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(addressComponents);
        dest.writeValue(formattedAddress);
        dest.writeValue(geometry);
        dest.writeValue(placeId);
        dest.writeList(types);
    }

    public int describeContents() {
        return 0;
    }

}