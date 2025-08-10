package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.weatherapp.api.WeatherApi;
import com.example.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView locationName, temperature, windSpeed, windGust, windDirection;
    private ImageView weatherIcon, windDirectionArrow;

    private WeatherApi api;

    public void setApi(WeatherApi api) {
        this.api = api;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("API Key Check", "API key from BuildConfig: '" + BuildConfig.WEATHER_API_KEY + "'");
        Log.d("API Key Check", "API key length: " + BuildConfig.WEATHER_API_KEY.length());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationName = findViewById(R.id.locationName);
        temperature = findViewById(R.id.temperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        windSpeed = findViewById(R.id.windSpeed);
        windGust = findViewById(R.id.windGust);
        windDirection = findViewById(R.id.windDirection);
        windDirectionArrow = findViewById(R.id.windDirectionArrow);

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
        Call<WeatherResponse> call = api.getCurrentWeather("67f0ab0e5b954496b02160817250908", location);
        call.enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    long windSpeedRounded = Math.round(weather.current.wind_mph);
                    long windGustRounded = Math.round(weather.current.gust_mph);

                    Log.d("MainActivity", "API Success: Location = " + weather.location.name + ", Temp = " + weather.current.temp_c + ", Wind Speed =" + windSpeedRounded);

                    runOnUiThread(() -> {
                        locationName.setText(weather.location.name + ", " + weather.location.region);
                        temperature.setText(weather.current.temp_c + "°C");
                        windSpeed.setText("Wind Speed: " + windSpeedRounded + " mph");
                        windGust.setText("Wind Gust: " + windGustRounded + " mph");
                        windDirection.setText("Wind Direction: " + weather.current.wind_dir);

                        float rotation = (weather.current.wind_degree + 180) % 360;
                        windDirectionArrow.setRotation(rotation);
                        // ← This is where you add the Glide code to load the icon
                        String iconUrl = "https:" + weather.current.condition.icon;
                        Glide.with(MainActivity.this).load(iconUrl).into(weatherIcon);
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
