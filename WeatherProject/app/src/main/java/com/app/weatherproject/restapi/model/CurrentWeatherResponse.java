package com.app.weatherproject.restapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CurrentWeatherResponse implements Parcelable {

    private List<Weather> weather;
    private Main main;
    private long dt;
    private int id;
    private Wind wind;
    private String name;

    protected CurrentWeatherResponse(Parcel in) {
        dt = in.readLong();
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<CurrentWeatherResponse> CREATOR = new Creator<CurrentWeatherResponse>() {
        @Override
        public CurrentWeatherResponse createFromParcel(Parcel in) {
            return new CurrentWeatherResponse(in);
        }

        @Override
        public CurrentWeatherResponse[] newArray(int size) {
            return new CurrentWeatherResponse[size];
        }
    };

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(dt);
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
