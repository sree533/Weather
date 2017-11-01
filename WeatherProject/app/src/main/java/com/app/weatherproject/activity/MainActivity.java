package com.app.weatherproject.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.weatherproject.R;
import com.app.weatherproject.restapi.api.BaseURL;
import com.app.weatherproject.restapi.api.WeatherAPI;
import com.app.weatherproject.restapi.model.CurrentWeatherResponse;
import com.app.weatherproject.restapi.model.Main;
import com.app.weatherproject.restapi.model.Weather;
import com.app.weatherproject.restapi.model.Wind;
import com.app.weatherproject.util.Constants;
import com.app.weatherproject.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_FILE_NAME = "weather_pref";
    private static final String PREF_KEY_WEATHER = "weather_response";
    private static final String PREF_KEY_CITY = "city";

    @Bind(R.id.inputSearch)
    SearchView inputSearch;

    @Bind(R.id.todayDate)
    TextView todayDate;

    @Bind(R.id.currentTemp)
    TextView currentTemp;

    @Bind(R.id.artImage)
    ImageView tempImage;

    @Bind(R.id.minTemp)
    TextView minTemp;

    @Bind(R.id.tempCondition)
    TextView tempCondition;

    @Bind(R.id.humidity)
    TextView humidity;

    @Bind(R.id.pressure)
    TextView pressure;

    @Bind(R.id.wind)
    TextView windSpeed;

    @Bind(R.id.cityName)
    TextView cityName;

    @Bind(R.id.noResults)
    TextView noResults;

    @Bind(R.id.cardLayout)
    CardView cardView;

    private WeatherAPI weatherAPI;

    private final SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // Make an API call only if the city is present
            if (!TextUtils.isEmpty(query)) {
                // Hide keyboard after search
                inputSearch.clearFocus();
                prepareAPICall(query);
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String updatedQuery) {
            if (TextUtils.isEmpty(updatedQuery)) {
                inputSearch.setQueryHint(getString(R.string.search_city));
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        weatherAPI = BaseURL.getAPI();

        // Auto load of last saved city's weather info
        if (!TextUtils.isEmpty(getSavedCityName())) {
            setUI(buildResponseFromSharedPreferences());
        }
        setupSearchView();
    }

    /**
     * Method to set search view
     */
    private void setupSearchView() {
        final EditText searchEditText = inputSearch.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setTextColor(Color.GRAY);
        }
        // Auto load of last saved city name
        if (!TextUtils.isEmpty(getSavedCityName())) {
            inputSearch.setQuery(getSavedCityName(), false);
            inputSearch.setFocusable(false);
        }

        inputSearch.setOnQueryTextListener(onQueryTextListener);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            inputSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
    }

    /**
     * Method to make current weather web service call
     */
    private void prepareAPICall(final String cityQuery) {
        Call<CurrentWeatherResponse> call = weatherAPI.getCurrentWeather(cityQuery, Constants.UNITS, Constants.APP_ID);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Open Weather Map apis doesn't have caching support internally, so using shared preference to save the data.
                    saveInSharedPreferences(response.body(), cityQuery);
                    setUI(response.body());
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                showErrorMessage();
                Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method to set data to the UI
     */
    private void setUI(CurrentWeatherResponse weatherResponse) {
        hideErrorMessage();
        if (weatherResponse == null) {
            return;
        }
        final String convertedDate = DateUtil.convertEpochToString(weatherResponse.getDt());
        todayDate.setText(convertedDate);
        cityName.setText(weatherResponse.getName());

        final List<Weather> weatherList = weatherResponse.getWeather();
        if (weatherList != null && weatherList.size() > 0) {
            Weather weather = weatherList.get(0);
            if (weather != null) {
                if (!TextUtils.isEmpty(weather.getIcon())) {
                    Picasso.with(this).load(String.format(Constants.IMAGE_URL, weather.getIcon())).into(tempImage);
                }
                tempCondition.setText(weather.getMain());
            }
        }

        final Main main = weatherResponse.getMain();
        if (main != null) {
            final String current = String.format(Locale.US, "%.1f%s", main.getTemp(), Constants.DEGREE_SYMBOL);
            final String min = String.format(Locale.US, "%.1f%s", main.getTempMin(), Constants.DEGREE_SYMBOL);
            humidity.setText(String.format(Locale.US, "%s:%.1f%s", "Humidity", main.getHumidity(), " %"));
            pressure.setText(String.format(Locale.US, "%s:%.1f%s", "Pressure", main.getPressure(), " hPa"));
            currentTemp.setText(current);
            minTemp.setText(min);
        }

        final Wind wind = weatherResponse.getWind();
        if (wind != null) {
            windSpeed.setText(String.format(Locale.US, "%s:%.1f%s", "Wind", wind.getSpeed(), " km/h NW"));
        }
    }

    /**
     * Method to show error message instead of card view in case of errors
     */
    private void showErrorMessage() {
        noResults.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
    }

    /**
     * Method to show card view with all weather details
     */
    private void hideErrorMessage() {
        noResults.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }

    /**
     * Method to get saved weather response from shared preferences
     *
     * @return CurrentWeatherResponse
     */
    private CurrentWeatherResponse buildResponseFromSharedPreferences() {
        final SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        final String gsonMapString = sharedPreferences.getString(PREF_KEY_WEATHER, Constants.EMPTY_STRING);
        final Gson gson = new Gson();
        // Need to use reflection to get back the response from string
        final java.lang.reflect.Type type = new TypeToken<CurrentWeatherResponse>() {
        }.getType();
        return gson.fromJson(gsonMapString, type);
    }

    /**
     * Method to get saved city name from preferences
     *
     * @return city
     */
    private String getSavedCityName() {
        final SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_KEY_CITY, Constants.EMPTY_STRING);
    }

    /**
     * Method to save response and city to shared preferences
     *
     * @param weatherResponse weather response
     * @param cityQuery       city name
     */
    private void saveInSharedPreferences(CurrentWeatherResponse weatherResponse, String cityQuery) {
        // Convert weather response to string using gson, as shared preference can't store the entire object
        final Gson gson = new Gson();
        final String responseGSON = gson.toJson(weatherResponse);
        final SharedPreferences.Editor editor = getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE).edit();

        editor.putString(PREF_KEY_WEATHER, responseGSON);
        editor.putString(PREF_KEY_CITY, cityQuery);
        editor.apply();
    }
}
