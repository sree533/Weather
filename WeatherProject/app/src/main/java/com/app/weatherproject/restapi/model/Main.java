package com.app.weatherproject.restapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Main implements Parcelable {

    private double temp;
    private double pressure;
    private double humidity;
    private double tempMin;
    private double tempMax;

    protected Main(Parcel in) {
        temp = in.readDouble();
        pressure = in.readDouble();
        humidity = in.readDouble();
        tempMin = in.readDouble();
        tempMax = in.readDouble();
    }

    public static final Creator<Main> CREATOR = new Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(temp);
        parcel.writeDouble(pressure);
        parcel.writeDouble(humidity);
        parcel.writeDouble(tempMin);
        parcel.writeDouble(tempMax);
    }
}
