package com.example.weatherapp.model;

import java.util.List;

public class GeocodeResponse {
    public List<Result> results;
    public String status;

    public static class Result {
        public List<AddressComponent> address_components;
        public String formatted_address;
    }

    public static class AddressComponent {
        public String long_name;
        public String short_name;
        public List<String> types;
    }
}

