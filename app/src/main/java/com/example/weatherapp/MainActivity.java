package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.weatherapp.adapters.DayAdapter;
import com.example.weatherapp.adapters.HourlyAdapter;
import com.example.weatherapp.api.GoogleGeocodingApi;
import com.example.weatherapp.api.GooglePlacesApi;
import com.example.weatherapp.api.GoogleWeatherApi;
import com.example.weatherapp.api.RetrofitClient;
import com.example.weatherapp.model.GeocodeResponse;
import com.example.weatherapp.model.GoogleWeatherResponseCurrent;
import com.example.weatherapp.model.GoogleWeatherResponseDaily;
import com.example.weatherapp.model.GoogleWeatherResponseHourly;
import com.example.weatherapp.utils.DateFormatter;
import com.example.weatherapp.utils.LocationUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity {

    private TextView locationName, temperature, feelsLikeTemperature, windSpeed, hourTime,
            currentDate, rainChance, rainAmount,
            windGust,
            windDirection;
    private ImageView weatherIcon, windDirectionArrow;

    private GoogleWeatherApi googleWeatherApi;
    private GooglePlacesApi googlePlacesApi;
    private GoogleGeocodingApi googleGeocodingApi;

    private RecyclerView recyclerHourlyView;
    private RecyclerView recyclerDayView;
    private FusedLocationProviderClient fusedLocationClient;

    private double defaultLatitude;
    private double defaultLongitude;

    public void setApi(GoogleWeatherApi googleWeatherApi) { this.googleWeatherApi = googleWeatherApi;}

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private void checkLocationPermissionAndFetch() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            fetchDefaultLocationAndWeather();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchDefaultLocationAndWeather();
            } else {
                // Permission denied — fallback to default coordinates
                defaultLatitude = 55.0170;
                defaultLongitude = -1.4250;
                fetchGoogleWeather(BuildConfig.WEATHER_API_KEY, defaultLatitude, defaultLongitude);
                fetchCityName(BuildConfig.PLACES_API_KEY, defaultLatitude, defaultLongitude);
            }
        }
    }



    private void fetchDefaultLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                defaultLatitude = location.getLatitude();
                defaultLongitude = location.getLongitude();
            } else {
                // fallback to a fixed location if location is unavailable
                defaultLatitude = 58.0170;
                defaultLongitude = -1.4250;
            }
            // Fetch weather and city for this default location
            fetchCityName(BuildConfig.PLACES_API_KEY, defaultLatitude, defaultLongitude);
            fetchGoogleWeather(BuildConfig.WEATHER_API_KEY, defaultLatitude, defaultLongitude);
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("API Key Check", "API key length: " + BuildConfig.WEATHER_API_KEY.length());
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.PLACES_API_KEY);
        }
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "Setting Content VIew");
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Content set");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        String weatherApiKey = BuildConfig.WEATHER_API_KEY;
        String placesApiKey = BuildConfig.PLACES_API_KEY;

        currentDate = findViewById(R.id.currentDate);
        locationName = findViewById(R.id.locationName);
        temperature = findViewById(R.id.temperature);
        feelsLikeTemperature = findViewById(R.id.feelsLikeTemperature);
        rainChance = findViewById(R.id.rainChance);
        rainAmount = findViewById(R.id.rainAmount);
        weatherIcon = findViewById(R.id.weatherIcon);
        windSpeed = findViewById(R.id.windSpeed);
        windGust = findViewById(R.id.windGust);
        windDirection = findViewById(R.id.windDirection);
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

        SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            // Call your refresh logic here (e.g., reload weather data)
            checkLocationPermissionAndFetch();

            // Stop refreshing once done
            swipeRefresh.setRefreshing(false);
        });


        createAutoCompleteFragment(weatherApiKey, placesApiKey);

        // Setup Retrofit
        googleGeocodingApi = RetrofitClient.getGeocodingApi();
        googleWeatherApi = RetrofitClient.getGoogleWeatherApi();
        googlePlacesApi = RetrofitClient.getGooglePlacesApi();

        // Call Apis
        checkLocationPermissionAndFetch();
//        fetchDefaultLocationAndWeather();
//        fetchCityName(placesApiKey,55.0170, -1.4250);
//        fetchGoogleWeather(weatherApiKey,55.0170, -1.4250);
    }

    public void fetchGoogleWeather(String apiKey, double latitude, double longitude) {
        fetchCurrentWeather(apiKey, latitude, longitude);
        fetchHourlyWeather(apiKey, latitude, longitude);
        fetchDailyWeather(apiKey, latitude, longitude);
    }

    private void fetchDailyWeather(String apiKey, double latitude, double longitude) {
        Call<GoogleWeatherResponseDaily> googleDailyCall = googleWeatherApi.getDaily(apiKey, latitude, longitude, 7);
        googleDailyCall.enqueue(new Callback<GoogleWeatherResponseDaily>() {

            @Override
            public void onResponse(Call<GoogleWeatherResponseDaily> call, Response<GoogleWeatherResponseDaily> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GoogleWeatherResponseDaily googleWeatherResponseDaily = response.body();

                    Log.d("MainActivity", "Requested 7 days, got " + googleWeatherResponseDaily.forecastDays.size() + " days");
                    Log.d("MainActivity", "Daily date: " + googleWeatherResponseDaily.forecastDays.get(0).interval.startTime +
                            " - " + googleWeatherResponseDaily.forecastDays.get(0).interval.endTime);

                    runOnUiThread(() -> {
                        List<GoogleWeatherResponseDaily.ForecastDay> forecastDays = googleWeatherResponseDaily.forecastDays;
                        DayAdapter dayAdapter = new DayAdapter(forecastDays);
                        recyclerDayView.setAdapter(dayAdapter);
                    });



                } else {
                    Log.d("MainActivity", "API response not successful or body is null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GoogleWeatherResponseDaily> call, Throwable t) {
                Log.d("MainActivity", "Failed to fetch google weather: " + t.getMessage());
            }
        });
    }

    private void fetchHourlyWeather(String apiKey, double latitude, double longitude) {
        Call<GoogleWeatherResponseHourly> googleHourlyCall = googleWeatherApi.getHourly(apiKey, latitude, longitude);
        googleHourlyCall.enqueue(new Callback<GoogleWeatherResponseHourly>() {

            @Override
            public void onResponse(Call<GoogleWeatherResponseHourly> call, Response<GoogleWeatherResponseHourly> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GoogleWeatherResponseHourly googleWeatherResponseHourly = response.body();
                    Log.d("MainActivity", "Hourly timezone: " + googleWeatherResponseHourly.timeZone.id);
                    Log.d("MainActivity", "Current time hourly: " + googleWeatherResponseHourly.forecastHours.get(0).interval.startTime + " - " +
                            googleWeatherResponseHourly.forecastHours.get(0).interval.endTime);
                    Log.d("MainActivity", "Current temp hourly: " + googleWeatherResponseHourly.forecastHours.get(0).temperature.degrees);
                    runOnUiThread(() -> {
                        List<GoogleWeatherResponseHourly.ForecastHour> hours = googleWeatherResponseHourly.forecastHours;
                        HourlyAdapter hourlyAdapter = new HourlyAdapter(hours);
                        recyclerHourlyView.setAdapter(hourlyAdapter);

                    });
                } else {
                    Log.d("MainActivity", "API response not successful or body is null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GoogleWeatherResponseHourly> call, Throwable t) {
                Log.d("MainActivity", "Failed to fetch google weather: " + t.getMessage());
            }
        });
    }

    private void fetchCurrentWeather(String apiKey, double latitude, double longitude) {
        Call<GoogleWeatherResponseCurrent> googleCall = googleWeatherApi.getCurrent(apiKey, latitude, longitude);
        googleCall.enqueue(new Callback<GoogleWeatherResponseCurrent>() {

            @Override
            public void onResponse(Call<GoogleWeatherResponseCurrent> call, Response<GoogleWeatherResponseCurrent> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GoogleWeatherResponseCurrent googleWeatherResponseCurrent = response.body();
                    Log.d("MainActivity", "Current time: " + googleWeatherResponseCurrent.currentTime);
                    Log.d("MainActivity", "Current temp: " + googleWeatherResponseCurrent.temperature.degrees);
                    String formattedDate = DateFormatter.formatDate(googleWeatherResponseCurrent.currentTime);

                    runOnUiThread(() -> {
                        currentDate.setText(formattedDate);
                        temperature.setText((int) Math.round(googleWeatherResponseCurrent.temperature.degrees) + "°");
                        feelsLikeTemperature.setText("Feels like " + (int) Math.round(googleWeatherResponseCurrent.feelsLikeTemperature.degrees) + "°");
                        rainChance.setText(googleWeatherResponseCurrent.precipitation.probability.percent + "%");
                        rainAmount.setText((int) googleWeatherResponseCurrent.precipitation.qpf.quantity + "mm");
                        windSpeed.setText(Math.round(mphConverter(googleWeatherResponseCurrent.wind.speed.value)) + " mph");
                        windGust.setText(Math.round(mphConverter(googleWeatherResponseCurrent.wind.gust.value)) + " mph");
//                        windDirection.setText(googleWeatherResponseCurrent.wind.direction.cardinal);
//                        windDirection.setText("Wind Direction: " + weather.current.wind_dir);

                        float rotation = (googleWeatherResponseCurrent.wind.direction.degrees + 180) % 360;
                        windDirectionArrow.setRotation(rotation);

                        // Glide code to load the icon
                        Log.d("MainActivity", "icon url: " + googleWeatherResponseCurrent.weatherCondition.iconBaseUri);
                        String iconUrl = googleWeatherResponseCurrent.weatherCondition.iconBaseUri + ".png";

                        Glide.with(MainActivity.this)
                                .load(iconUrl)
                                .error(R.drawable.ic_arrow) // fallback drawable
                                .listener(new RequestListener<Drawable>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.e("MainActivity", "Failed to load icon", e);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.d("MainActivity", "Icon loaded successfully");
                                        return false;
                                    }
                                })
                                .into(weatherIcon);
                    });
                }
                else {
                    Log.d("MainActivity", "API response not successful or body is null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GoogleWeatherResponseCurrent> call, Throwable t) {
                Log.d("MainActivity", "Failed to fetch google weather: " + t.getMessage());
            }
        });
    }

    private void fetchCityName(String apiKey, double latitude, double longitude) {
        String latLng = latitude + "," + longitude;

        Call<GeocodeResponse> call = googleGeocodingApi.getCityFromCoordinates(
                latLng,
                apiKey
        );

        call.enqueue(new Callback<GeocodeResponse>() {
            @Override
            public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().results.isEmpty()) {
                    String displayName = LocationUtils.buildLocationString(response.body().results.get(0));

                    for (GeocodeResponse.Result result : response.body().results) {
                        for (GeocodeResponse.AddressComponent component : result.address_components) {
                            if (component.types.contains("locality")) {
                                String cityName = component.long_name;
                                Log.d("MainActivity", "City: " + cityName);

                                runOnUiThread(() -> locationName.setText(cityName + displayName));
                                return;
                            }
                        }
                    }

                    // fallback if "locality" not found
                    String fallback = response.body().results.get(0).formatted_address;
                    runOnUiThread(() -> locationName.setText(fallback));
                } else {
                    Log.e("MainActivity", "No results from geocoding API");
                }
            }

            @Override
            public void onFailure(Call<GeocodeResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private void createAutoCompleteFragment(String weatherApiKey, String placesApiKey) {
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
                if (place.getLatLng() != null) {
                    double latitude = place.getLatLng().latitude;
                    double longitude = place.getLatLng().longitude;
                    fetchGoogleWeather(weatherApiKey, latitude, longitude);
                    fetchCityName(placesApiKey, latitude, longitude);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("Places", "Error: " + status);
            }
        });
    }

    private double mphConverter(double kph){
        return kph * 0.621371;
    }
}