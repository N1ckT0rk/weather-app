package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesNearbyResponse {
    @SerializedName("results")
    public List<PlaceResult> results;

    public static class PlaceResult {
        @SerializedName("name")
        public String name;

        @SerializedName("vicinity")
        public String vicinity;
    }
}

