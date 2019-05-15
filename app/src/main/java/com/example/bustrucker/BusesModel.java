package com.example.bustrucker;

import android.os.Parcel;
import android.os.Parcelable;

public class BusesModel implements Parcelable {
    int id ;
    String fromLoc,toLoc ;
    boolean status;
    double latitude ,longitude;
//here_______________________________________________________________________________________
    public BusesModel(){}

    public BusesModel(int id ,String fromLoc ,String toLoc ,boolean status ,double latitude ,double longitude)
    {
        this.id =id ;
        this.fromLoc=fromLoc ;
        this.toLoc=toLoc ;
        this.latitude=latitude ;
        this.longitude =longitude ;
    }

    public int getId() {
        return id;
    }

    public String getFromLoc() {
        return fromLoc;
    }

    public String getToLoc() {
        return toLoc;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
