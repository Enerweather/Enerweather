package org.ulpgc.dacd.infrastructure.rest;

import org.ulpgc.dacd.application.port.GetWeatherUseCase;
import org.ulpgc.dacd.domain.model.Weather;
import org.ulpgc.dacd.infrastructure.accessors.WeatherFetchException;

public class WeatherController{
    private final GetWeatherUseCase getWeather;
    public WeatherController(GetWeatherUseCase getWeather) {
        this.getWeather = getWeather;
    }

    public Weather getWeatherData(String city) throws WeatherFetchException {
        return getWeather.execute(city);
    }
}