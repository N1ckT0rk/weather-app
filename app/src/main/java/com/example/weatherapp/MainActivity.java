package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.adapters.DayAdapter;
import com.example.weatherapp.adapters.HourlyAdapter;
import com.example.weatherapp.api.WeatherApi;
import com.example.weatherapp.model.WeatherResponse;
import com.example.weatherapp.utils.DateFormatter;
import com.example.weatherapp.utils.LocationUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private TextView locationName, temperature, feelsLikeTemperature, windSpeed, hourTime,
            windGust,
            windDirection;
    private ImageView weatherIcon, windDirectionArrow;

    private WeatherApi api;
    private RecyclerView recyclerHourlyView;
    private RecyclerView recyclerDayView;


    public LocationUtils locationUtils;

    public void setApi(WeatherApi api) {
        this.api = api;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("API Key Check", "API key from BuildConfig: '" + BuildConfig.WEATHER_API_KEY + "'");
        Log.d("API Key Check", "API key length: " + BuildConfig.WEATHER_API_KEY.length());
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.PLACES_API_KEY);
        }
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "Setting Content VIew");
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Content set");

        locationName = findViewById(R.id.locationName);
        temperature = findViewById(R.id.temperature);
        feelsLikeTemperature = findViewById(R.id.feelsLikeTemperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        windSpeed = findViewById(R.id.windSpeed);
//        windGust = findViewById(R.id.windGust);
//        windDirection = findViewById(R.id.windDirection);
        windDirectionArrow = findViewById(R.id.windDirectionArrow);
//        hourTime = findViewById(R.id.hourTime);
        recyclerHourlyView = findViewById(R.id.recyclerHourly);
        recyclerHourlyView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerDayView = findViewById(R.id.recyclerDay);
        recyclerDayView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );


        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.autocompleteFragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS

        ));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String location = locationUtils.buildLocationString(place);
                if (!location.isEmpty()) {
                    fetchWeather(location);
                } else {
                    // fallback if no address components, use place name
                    String placeName = place.getName();
                    if (placeName != null && !placeName.isEmpty()) {
                        fetchWeather(placeName);
                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("Places", "Error: " + status);
            }
        });


        // Setup Retrofit
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.weatherapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(WeatherApi.class);
        }
        // Fetch weather data for Tynemouth
        fetchWeather("Tynemouth");
    }

    public void fetchWeather(String location) {
        Log.d("MainActivity", "API Key length: " + BuildConfig.WEATHER_API_KEY.length());
        Log.d("API Key", BuildConfig.WEATHER_API_KEY);
        Log.d("MainActivity", "Fetching weather for: " + location);
        Call<WeatherResponse> call = api.getForecast(BuildConfig.WEATHER_API_KEY, location, 7);
        call.enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    long windSpeedRounded = Math.round(weather.current.wind_mph);
                    long windGustRounded = Math.round(weather.current.gust_mph);

                    Log.d("MainActivity", "API Success: Location = " + weather.location.name + ", Temp = " +
                            weather.current.temp_c + ", Wind Speed = " + windSpeedRounded + ", Gust = " + weather.current.gust_mph +
                            ", Todays Max Temp = " + weather.forecast.forecastday.get(0).day.maxtemp_c + "Time: " + weather.forecast.forecastday.get(0).hour.get(0).time);

                    runOnUiThread(() -> {
                        locationName.setText(weather.location.name + ", " + weather.location.region);
                        int temperatureInt = (int) Math.round(weather.current.temp_c);
                        temperature.setText(temperatureInt + "°");
                        int feelsLikeInt = (int) Math.round(weather.current.feelslike_c);
                        feelsLikeTemperature.setText("Feels like " + feelsLikeInt + "°");
                        windSpeed.setText(windSpeedRounded + " mph");
//                        windGust.setText("Wind Gust: " + windGustRounded + " mph");
//                        windDirection.setText("Wind Direction: " + weather.current.wind_dir);
//                        String hourTimeNotFormatted = weather.forecast.forecastday.get(0).hour.get(0).time;
//                        String hourTimeFormatted = DateFormatter.formatHour(hourTimeNotFormatted);
//                        hourTime.setText(hourTimeFormatted);

                        float rotation = (weather.current.wind_degree + 180) % 360;
                        windDirectionArrow.setRotation(rotation);
                        // ← This is where you add the Glide code to load the icon
                        String iconUrl = "https:" + weather.current.condition.icon;
                        Glide.with(MainActivity.this).load(iconUrl).into(weatherIcon);

                        List<WeatherResponse.Hour> hours = weather.forecast.forecastday.get(0).hour;
                        HourlyAdapter hourlyAdapter = new HourlyAdapter(hours);
                        recyclerHourlyView.setAdapter(hourlyAdapter);

                        List<WeatherResponse.ForecastDay> forecastDays = weather.forecast.forecastday;
                        DayAdapter dayAdapter = new DayAdapter(forecastDays);
                        recyclerDayView.setAdapter(dayAdapter);
                        Log.d("DayAdapter", "Number of forecast days: " + forecastDays.size());


                    });
                } else {
                    Log.d("MainActivity", "API response not successful or body is null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle failure - show error or retry
                Log.e("MainActivity", "API call failed: " + t.getMessage());
            }
        });
    }
}