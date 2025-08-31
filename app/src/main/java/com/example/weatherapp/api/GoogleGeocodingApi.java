package com.example.weatherapp.api;

import com.example.weatherapp.model.GeocodeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleGeocodingApi {
    @GET("maps/api/geocode/json")
    Call<GeocodeResponse> getCityFromCoordinates(
            @Query("latlng") String latlng,
            @Query("key") String apiKey
    );
}
