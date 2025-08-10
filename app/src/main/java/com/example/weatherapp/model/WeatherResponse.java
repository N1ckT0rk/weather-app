package com.example.weatherapp.model;

public class WeatherResponse {
    public Location location;
    public Current current;

    public class Location {
        public String name;
        public String region;
        public String country;
    }

    public class Current {
        public double temp_c;
        public Condition condition;

        public double wind_mph;

        public double gust_mph;

        public String wind_dir;

        public int wind_degree;

        public class Condition {
            public String text;

            public String icon;
        }
    }
}

