package com.example.weatherapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static GoogleWeatherApi googleWeatherApi;
    private static GooglePlacesApi googlePlacesApi;
    private static GoogleGeocodingApi googleGeocodingApi;

    public static GoogleGeocodingApi getGeocodingApi() {
        // Setup Retrofit
        if (googleGeocodingApi == null) {
            Retrofit retrofitGeocode = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            googleGeocodingApi = retrofitGeocode.create(GoogleGeocodingApi.class);
        }
        return googleGeocodingApi;
    }
    public static GooglePlacesApi getGooglePlacesApi() {
        // Setup Retrofit
        if (googlePlacesApi == null) {
            Retrofit retrofitPlaces = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            googlePlacesApi = retrofitPlaces.create(GooglePlacesApi.class);
        }

        return googlePlacesApi;
    }
    public static GoogleWeatherApi getGoogleWeatherApi() {
        if (googleWeatherApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://weather.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            googleWeatherApi = retrofit.create(GoogleWeatherApi.class);
        }
        return googleWeatherApi;
    }
}
