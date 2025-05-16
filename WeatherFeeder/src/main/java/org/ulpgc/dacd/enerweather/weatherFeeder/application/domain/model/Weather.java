package org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Weather {
    private final double windSpeed;
    private final String description;
    private final String cityName;
    private final String date;

    public Weather(double windSpeed, String description, String cityName) {
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
