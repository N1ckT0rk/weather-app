package com.example.weatherapp.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {

    public static String formatDate(String currentTime) {
        OffsetDateTime odt = OffsetDateTime.parse(currentTime);

//      Format to "Tue, 28 Jan"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.getDefault());
        return odt.format(formatter);
    }
}
