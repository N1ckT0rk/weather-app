package com.example.weatherapp.api;

import com.example.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherApi {
    @GET("v1/currentConditions:lookup")
    Call<WeatherResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int numberOfDays
    );

//    @GET("v1/forecast.json")
//    Call<WeatherResponse> getHourlyWeather(
//            @Query("key") String apiKey,
//            @Query("q") String location,
//            @Query("days") int numberOfDays
//    );
}

