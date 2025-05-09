package org.ulpgc.dacd.infrastructure.rest;

import org.ulpgc.dacd.application.port.GetWeatherUseCase;
import org.ulpgc.dacd.domain.model.Weather;

public class WeatherController{
    private final GetWeatherUseCase getWeather;
    public WeatherController(GetWeatherUseCase getWeather) {
        this.getWeather = getWeather;
    }

    public Weather getWeatherData(String city){
        return getWeather.execute(city);
    }
}