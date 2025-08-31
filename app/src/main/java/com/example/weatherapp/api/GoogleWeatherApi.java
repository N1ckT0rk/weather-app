package com.example.weatherapp.api;

import com.example.weatherapp.model.GoogleWeatherResponseCurrent;
import com.example.weatherapp.model.GoogleWeatherResponseDaily;
import com.example.weatherapp.model.GoogleWeatherResponseHourly;
import com.example.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleWeatherApi {
    @GET("/v1/currentConditions:lookup")
    Call<GoogleWeatherResponseCurrent> getCurrent(
            @Query("key") String apiKey,
            @Query("location.latitude") double latitude,
            @Query("location.longitude") double longitude
    );

    @GET("/v1/forecast/hours:lookup")
    Call<GoogleWeatherResponseHourly> getHourly(
            @Query("key") String apiKey,
            @Query("location.latitude") double latitude,
            @Query("location.longitude") double longitude
    );

    @GET("/v1/forecast/days:lookup")
    Call<GoogleWeatherResponseDaily> getDaily(
            @Query("key") String apiKey,
            @Query("location.latitude") double latitude,
            @Query("location.longitude") double longitude,
            @Query("days") int days
    );

}
