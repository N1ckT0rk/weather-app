package com.example.weatherapp.model;

public class GoogleWeatherResponseCurrent {

    public String currentTime;
    public TimeZone timeZone;
    public boolean isDaytime;
    public WeatherCondition weatherCondition;
    public Temperature temperature;
    public Temperature feelsLikeTemperature;
    public Temperature dewPoint;
    public Temperature heatIndex;
    public Temperature windChill;
    public int relativeHumidity;
    public int uvIndex;
    public Precipitation precipitation;
    public int thunderstormProbability;
    public AirPressure airPressure;
    public Wind wind;
    public Visibility visibility;
    public int cloudCover;
    public CurrentConditionsHistory currentConditionsHistory;

    public static class TimeZone {
        public String id;
    }

    public static class WeatherCondition {
        public String iconBaseUri;
        public Description description;
        public String type;

        public static class Description {
            public String text;
            public String languageCode;
        }
    }

    public static class Temperature {
        public double degrees;
        public String unit;
    }

    public static class Precipitation {
        public Probability probability;
        public Qpf qpf;

        public static class Probability {
            public int percent;
            public String type;
        }

        public static class Qpf {
            public double quantity;
            public String unit;
        }
    }

    public static class AirPressure {
        public double meanSeaLevelMillibars;
    }

    public static class Wind {
        public Direction direction;
        public Speed speed;
        public Gust gust;

        public static class Direction {
            public int degrees;
            public String cardinal;
        }

        public static class Speed {
            public int value;
            public String unit;
        }

        public static class Gust {
            public int value;
            public String unit;
        }
    }

    public static class Visibility {
        public int distance;
        public String unit;
    }

    public static class CurrentConditionsHistory {
        public Temperature temperatureChange;
        public Temperature maxTemperature;
        public Temperature minTemperature;
        public Precipitation.Qpf qpf;
    }
}
