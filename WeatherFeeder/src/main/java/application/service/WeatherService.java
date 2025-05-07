package application.service;

import application.port.GetWeatherUseCase;
import application.port.WeatherFeeder;
import application.port.WeatherRepositoryPort;
import domain.model.Weather;

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
        return weatherData;
    }
}
