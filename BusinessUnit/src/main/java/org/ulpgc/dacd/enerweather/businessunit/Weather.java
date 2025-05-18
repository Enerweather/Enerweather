package org.ulpgc.dacd.enerweather.businessunit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Weather {
    private final double windSpeed;
    private final String description;
    private final String cityName;
    private final String timestamp;

    public Weather(double windSpeed, String description, String cityName) {
        this.windSpeed = windSpeed;
        this.description = description;
        this.cityName = cityName;
        this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString();
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private static final Gson GSON = new GsonBuilder().create();
    public static Weather fromJson(String json) {
        return GSON.fromJson(json, Weather.class);
    }
}
