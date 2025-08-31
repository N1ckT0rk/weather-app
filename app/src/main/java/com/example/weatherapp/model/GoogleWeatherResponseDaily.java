package com.example.weatherapp.model;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GoogleWeatherResponseDaily {

    public List<ForecastDay> forecastDays;
    public TimeZoneInfo timeZone;

    public static class ForecastDay {
        public Interval interval;
        public DisplayDate displayDate;
        public DaytimeForecast daytimeForecast;
        public NighttimeForecast nighttimeForecast;
        public Temperature maxTemperature;
        public Temperature minTemperature;
        public Temperature feelsLikeMaxTemperature;
        public Temperature feelsLikeMinTemperature;
        public SunEvents sunEvents;
        public MoonEvents moonEvents;
        public Temperature maxHeatIndex;
        public IceThickness iceThickness;
        public String getDayOfWeek() {
            LocalDate date = LocalDate.of(displayDate.year, displayDate.month, displayDate.day);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault());
            return date.format(formatter); // e.g. Mon, Tue, Wed
        }
    }

    public static class DisplayDate {
        public int year;
        public int month;
        public int day;
    }

    public static class Interval {
        public String startTime;
        public String endTime;
    }

    public static class DaytimeForecast {
        public Interval interval;
        public WeatherCondition weatherCondition;
        public int relativeHumidity;
        public int uvIndex;
        public Precipitation precipitation;
        public int thunderstormProbability;
        public Wind wind;
        public int cloudCover;
    }

    public static class NighttimeForecast {
        public Interval interval;
        public WeatherCondition weatherCondition;
        public int relativeHumidity;
        public int uvIndex;
        public Precipitation precipitation;
        public int thunderstormProbability;
        public Wind wind;
        public int cloudCover;
    }

    public static class WeatherCondition {
        public String iconBaseUri;
        public Description description;
        public String type;
    }

    public static class Description {
        public String text;
        public String languageCode;
    }

    public static class Temperature {
        public double degrees;
        public String unit;
    }

    public static class Wind {
        public Direction direction;
        public Speed speed;
        public Speed gust;
    }

    public static class Direction {
        public int degrees;
        public String cardinal;
    }

    public static class Speed {
        public double value;
        public String unit;
    }

    public static class Precipitation {
        public Probability probability;
        public Qpf qpf;
    }

    public static class Probability {
        public int percent;
        public String type;
    }

    public static class Qpf {
        public double quantity;
        public String unit;
    }

    public static class SunEvents {
        public String sunriseTime;
        public String sunsetTime;
    }

    public static class MoonEvents {
        public String moonPhase;
        public List<String> moonriseTimes;
        public List<String> moonsetTimes;
    }

    public static class IceThickness {
        public double thickness;
        public String unit;
    }

    public static class TimeZoneInfo {
        public String id;
    }

}

