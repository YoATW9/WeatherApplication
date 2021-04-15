package com.example.weatherapplication.OpenWeather;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenWeather {

    public static final String APP_ID = "6edd96d498ffab01ff671f39d92df7c1";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }
}
