// WeatherResponse.java
package com.example.weatherapp.model;

import java.util.List;

public class WeatherResponse {
    public Location location;
    public Current current;
    public Forecast forecast;

    public class Location {
        public String name;
        public String region;
        public String country;
        public double lat;
        public double lon;
        public String tz_id;
        public long localtime_epoch;
        public String localtime;
    }

    public class Current {
        public double temp_c;
        public double feelslike_c;
        public Condition condition;
        public double wind_kph;
        public double wind_mph;

        public int humidity;
        public double gust_mph;

        public String wind_dir;

        public int wind_degree;
    }

    public class Forecast {
        public List<ForecastDay> forecastday;
    }

    public class ForecastDay {
        public String date;
        public Day day;
        public Astro astro;
        public List<Hour> hour;
    }

    public class Day {
        public double maxtemp_c;
        public double mintemp_c;
        public double avgtemp_c;
        public double wind_mph;
        public double gust_mph;
        public String wind_dir;

        public int wind_degree;
        public Condition condition;
    }

    public class Astro {
        public String sunrise;
        public String sunset;
    }

    public class Hour {
        public String time;
        public double temp_c;
        public Condition condition;
        public double feelslike_c;
        public double wind_kph;
        public double wind_mph;

        public int humidity;
        public double gust_mph;

        public String wind_dir;

        public int wind_degree;
    }

    public class Condition {
        public String text;
        public String icon;
        public int code;
    }
}







//package com.example.weatherapp.model;
//
//public class WeatherResponse {
//    public Location location;
//    public Current current;
//
//    public class Location {
//        public String name;
//        public String region;
//        public String country;
//    }
//
//    public class Current {
//        public double temp_c;
//
//        public double feelslike_c;
//        public Condition condition;
//
//        public double wind_mph;
//
//        public double gust_mph;
//
//        public String wind_dir;
//
//        public int wind_degree;
//
//        public class Condition {
//            public String text;
//
//            public String icon;
//        }
//    }
//}

