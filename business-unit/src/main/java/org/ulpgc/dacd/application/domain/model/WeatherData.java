package org.ulpgc.dacd.application.domain.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class WeatherData {
    private final double windSpeed;
    private final String description;
    private final String cityName;
    private final String date;

    public WeatherData(double windSpeed, String description, String cityName) {
        this.windSpeed = windSpeed;
        this.description = description;
        this.cityName = cityName;
        this.date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString();
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

    public String getDate() {
        return date;
    }
}

