package com.app.weatherproject.restapi.api;


import com.app.weatherproject.restapi.model.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to have all API calls.
 */
public interface WeatherAPI {

    @GET("data/2.5/weather")
    Call<CurrentWeatherResponse> getCurrentWeather(@Query("q") String city, @Query("units") String units, @Query("APPID") String appID);

}
