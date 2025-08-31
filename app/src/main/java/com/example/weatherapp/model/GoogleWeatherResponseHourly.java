package com.example.weatherapp.model;

import java.util.List;

public class GoogleWeatherResponseHourly {

    public List<ForecastHour> forecastHours;
    public TimeZone timeZone;

    public static class ForecastHour {
        public Interval interval;
        public DisplayDateTime displayDateTime;
        public boolean isDaytime;
        public WeatherCondition weatherCondition;
        public Temperature temperature;
        public Temperature feelsLikeTemperature;
        public Temperature dewPoint;
        public Temperature heatIndex;
        public Temperature windChill;
        public Temperature wetBulbTemperature;
        public int relativeHumidity;
        public int uvIndex;
        public Precipitation precipitation;
        public int thunderstormProbability;
        public AirPressure airPressure;
        public Wind wind;
        public Visibility visibility;
        public int cloudCover;
        public IceThickness iceThickness;
    }

    public static class Interval {
        public String startTime;
        public String endTime;
    }

    public static class DisplayDateTime {
        public int year;
        public int month;
        public int day;
        public int hours;
        public String utcOffset;
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

    public static class AirPressure {
        public double meanSeaLevelMillibars;
    }

    public static class Wind {
        public Direction direction;
        public Speed speed;
        public Gust gust;
    }

    public static class Direction {
        public double degrees;
        public String cardinal;
    }

    public static class Speed {
        public double value;
        public String unit;
    }

    public static class Gust {
        public double value;
        public String unit;
    }

    public static class Visibility {
        public double distance;
        public String unit;
    }

    public static class IceThickness {
        public double thickness;
        public String unit;
    }

    public static class TimeZone {
        public String id;
    }
}
