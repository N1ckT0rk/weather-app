package com.example.weatherapp.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static String formatHour(String dateTimeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateTimeString);
            String hourOnly = outputFormat.format(date); // "00:00"
            Log.d("FormattedTime", hourOnly);
            return hourOnly;
        } catch (ParseException e) {
            e.printStackTrace();
            return dateTimeString; // fallback to original if parsing fails
        }
    }
}
