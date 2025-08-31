package com.example.weatherapp.api;

import com.example.weatherapp.model.PlacesNearbyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApi {
    @GET("maps/api/place/nearbysearch/json")
    Call<PlacesNearbyResponse> getNearbyLocalities(
            @Query("location") String latLng, // "lat,lng"
            @Query("radius") int radius,      // in meters
            @Query("type") String type,       // "locality"
            @Query("key") String apiKey
    );
}
