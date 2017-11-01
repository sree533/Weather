package com.app.weatherproject.restapi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class contains the base url and helper methods to create retrofit instance
 */
public class BaseURL {

    private static final String BASE_URL = "http://api.openweathermap.org/";

    private static Retrofit getBaseUrl() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WeatherAPI getAPI() {
        return BaseURL.getBaseUrl().create(WeatherAPI.class);
    }
}
