package com.vaisbleh.user.reuvenvaisblehfinalproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jbt on 17/09/2017.
 */

public class Place implements Parcelable {
    private String id ,name, address, picReference, phoneNum, iconLink, type;
    private double lon, lat;

    public Place(String id, String name, String address, String iconLink, double lon, double lat, String type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.iconLink = iconLink;
        this.lon = lon;
        this.lat = lat;
        this.type = type;
    }

    public Place(String id, String name, String address,
                 String picReference, String phoneNum, double lon, double lat) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.picReference = picReference;
        this.phoneNum = phoneNum;
        this.lon = lon;
        this.lat = lat;
    }

    protected Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        picReference = in.readString();
        phoneNum = in.readString();
        iconLink = in.readString();
        type = in.readString();
        lon = in.readDouble();
        lat = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicReference() {
        return picReference;
    }

    public void setPicReference(String picReference) {
        this.picReference = picReference;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(picReference);
        parcel.writeString(phoneNum);
        parcel.writeString(iconLink);
        parcel.writeString(type);
        parcel.writeDouble(lon);
        parcel.writeDouble(lat);
    }
}

