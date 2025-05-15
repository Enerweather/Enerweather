package org.ulpgc.dacd.enerweather.weatherFeeder.application.service;

import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.WeatherFeeder;
import org.ulpgc.dacd.enerweather.weatherFeeder.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.accessors.FetchException;

public class WeatherService {
    private final WeatherFeeder feeder;

    public WeatherService(WeatherFeeder feeder) {
        this.feeder = feeder;

    }


    public Weather getWeatherFor(String city) {
        try {
            return feeder.fetchCurrentWeather(city);
        } catch (FetchException e) {
            System.out.println("Error fetching weather for " + city);
            return null;
        }
    }
}
