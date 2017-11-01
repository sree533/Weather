package com.app.weatherproject.restapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wind implements Parcelable {

    private double speed;
    private double deg;

    protected Wind(Parcel in) {
        speed = in.readDouble();
        deg = in.readDouble();
    }

    public static final Creator<Wind> CREATOR = new Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(speed);
        parcel.writeDouble(deg);
    }
}
