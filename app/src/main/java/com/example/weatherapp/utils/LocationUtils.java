package com.example.weatherapp.utils;

import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

public class LocationUtils {
    public static String buildLocationString(Place place) {
        String city = "";
        String region = "";
        String country = "";

        if (place.getAddressComponents() != null) {
            for (AddressComponent component : place.getAddressComponents().asList()) {
                List<String> types = component.getTypes();
                if (types.contains("locality")) {
                    city = component.getName();
                } else if (types.contains("administrative_area_level_2")) {
                    region = component.getName();  // prefer this as the county/district
                } else if (types.contains("administrative_area_level_1")) {
                    if (region.isEmpty()) { // fallback if level_2 not set
                        region = component.getName();
                    }
                }

            }
        }

        // Build a combined location string
        StringBuilder locationBuilder = new StringBuilder();
        if (!city.isEmpty()) {
            locationBuilder.append(city);
        }
        if (!region.isEmpty()) {
            if (locationBuilder.length() > 0) locationBuilder.append(", ");
            locationBuilder.append(region);
        }
        if (!country.isEmpty()) {
            if (locationBuilder.length() > 0) locationBuilder.append(", ");
            locationBuilder.append(country);
        }
        return locationBuilder.toString();
    }
}
