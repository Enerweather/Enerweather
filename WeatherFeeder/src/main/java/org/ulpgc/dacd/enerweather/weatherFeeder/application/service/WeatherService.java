package org.ulpgc.dacd.enerweather.weatherFeeder.application.service;

import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.EventPublisher;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.GetWeatherUseCase;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.WeatherFeeder;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.WeatherRepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.domain.model.Weather;
import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.accessors.WeatherFetchException;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.messaging.MessagePublisher;

public class WeatherService implements GetWeatherUseCase {
    private final WeatherFeeder feeder;

    public WeatherService(WeatherFeeder feeder) {
        this.feeder = feeder;

    }

    @Override
    public Weather execute(String city) {
        try {
            return feeder.fetchCurrentWeather(city);
        } catch (WeatherFetchException e) {
            System.out.println("Error fetching weather for " + city);
            return null;
        }
    }
}
