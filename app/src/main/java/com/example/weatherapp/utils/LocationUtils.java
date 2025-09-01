package com.example.weatherapp.utils;

import com.example.weatherapp.model.GeocodeResponse;

import java.util.List;

public class LocationUtils {

    public static String buildLocationString(GeocodeResponse.Result result) {
        String regionName = null;

        for (GeocodeResponse.AddressComponent component : result.address_components) {
            List<String> types = component.types;
            if (types.contains("administrative_area_level_2")) {
                regionName = component.long_name;
            } else if (types.contains("administrative_area_level_1") && regionName == null) {
                regionName = component.long_name;
            }
        }


            if (regionName != null) {
                return ", " + regionName;
            }

        // fallback if no locality/postal_town
        return result.formatted_address;
    }

}



//        for (GeocodeResponse.Result result : results) {
//            String cityName = null;
//            String regionName = null;
//
//            for (GeocodeResponse.AddressComponent component : result.address_components) {
//                if (component.types.contains("postal_town") || component.types.contains("locality") || component.types.contains("sublocality_level_1")) {
//                    cityName = component.long_name;
//                } else if (component.types.contains("administrative_area_level_2")) {
//                    regionName = component.long_name;
//                } else if (component.types.contains("administrative_area_level_1") && regionName == null) {
//                    regionName = component.long_name;
//                }
//            }
//
//            if (cityName != null) {
//                String displayName = cityName;
//                if (regionName != null) displayName += ", " + regionName;
//                return displayName;
//            }
//        }
//
//        // fallback if no city found
//        return results.get(0).formatted_address;
//    }
//
//}
