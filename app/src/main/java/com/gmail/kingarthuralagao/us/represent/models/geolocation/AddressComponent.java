package com.gmail.kingarthuralagao.us.represent.models.geolocation;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressComponent implements Serializable, Parcelable
{
    @SerializedName("long_name")
    @Expose
    private String longName;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    public final static Parcelable.Creator<AddressComponent> CREATOR = new Creator<AddressComponent>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AddressComponent createFromParcel(Parcel in) {
            return new AddressComponent(in);
        }

        public AddressComponent[] newArray(int size) {
            return (new AddressComponent[size]);
        }

    };

    private final static long serialVersionUID = 4420160444710202331L;

    protected AddressComponent(Parcel in) {
        this.longName = ((String) in.readValue((String.class.getClassLoader())));
        this.shortName = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.types, (java.lang.String.class.getClassLoader()));
    }

    public AddressComponent() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(longName);
        dest.writeValue(shortName);
        dest.writeList(types);
    }

    public int describeContents() {
        return 0;
    }

}