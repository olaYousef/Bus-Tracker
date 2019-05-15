package com.example.bustrucker;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverModel implements Parcelable {


    int id ,busId ;
    String name , phoneNum ,email ;


    public DriverModel()
    {

    }

    public DriverModel(int id ,int busId ,String name ,String phoneNum ,String email )
    {
        this.id = id ;
        this.busId =busId ;
        this.name =name ;
        this.phoneNum =phoneNum ;
        this.email =email ;
    }

    protected DriverModel(Parcel in) {
        id = in.readInt();
        busId = in.readInt();
        name = in.readString();
        phoneNum = in.readString();
        email = in.readString();
    }

    public static final Creator<DriverModel> CREATOR = new Creator<DriverModel>() {
        @Override
        public DriverModel createFromParcel(Parcel in) {
            return new DriverModel(in);
        }

        @Override
        public DriverModel[] newArray(int size) {
            return new DriverModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getBusId() {
        return busId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(busId);
        parcel.writeString(name);
        parcel.writeString(phoneNum);
        parcel.writeString(email);
    }
}
