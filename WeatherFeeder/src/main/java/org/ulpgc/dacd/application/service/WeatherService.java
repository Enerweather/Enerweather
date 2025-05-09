package org.ulpgc.dacd.application.service;

import org.ulpgc.dacd.application.port.EventPublisher;
import org.ulpgc.dacd.application.port.GetWeatherUseCase;
import org.ulpgc.dacd.application.port.WeatherFeeder;
import org.ulpgc.dacd.application.port.WeatherRepositoryPort;
import org.ulpgc.dacd.domain.model.Weather;
import com.google.gson.Gson;
import org.ulpgc.dacd.infrastructure.messaging.MessagePublisher;

public class WeatherService implements GetWeatherUseCase {
    private final WeatherFeeder feeder;
    private final WeatherRepositoryPort repository;

    public WeatherService(WeatherFeeder feeder, WeatherRepositoryPort repository) {
        this.feeder = feeder;
        this.repository = repository;
    }

    @Override
    public Weather execute(String city) {
        Weather weatherData = feeder.fetchCurrentWeather(city);
        repository.save(weatherData);

        try {
            Gson gson = new Gson();
            String json = gson.toJson(weatherData);

            EventPublisher publisher = new MessagePublisher();
            publisher.publish(json);
            publisher.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return weatherData;
    }
}
