package Weather.application.service;

import Weather.application.port.GetWeatherUseCase;
import Weather.application.port.WeatherFeeder;
import Weather.application.port.WeatherRepositoryPort;
import Weather.domain.model.WeatherData;

public class WeatherService implements GetWeatherUseCase {
    private final WeatherFeeder feeder;
    private final WeatherRepositoryPort repository;

    public WeatherService(WeatherFeeder feeder, WeatherRepositoryPort repository) {
        this.feeder = feeder;
        this.repository = repository;
    }

    @Override
    public WeatherData execute(String city) {
        WeatherData weatherData = feeder.fetchCurrentWeather(city);
        repository.save(weatherData);
        return weatherData;
    }
}
