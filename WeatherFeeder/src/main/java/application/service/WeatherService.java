package application.service;

import application.port.GetWeatherUseCase;
import application.port.WeatherFeeder;
import application.port.WeatherRepositoryPort;
import domain.model.Weather;
import org.ulpgc.dacd.MessagePublisher;
import com.google.gson.Gson;

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

            MessagePublisher publisher = new MessagePublisher();
            publisher.sendMessage(json);
            publisher.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return weatherData;
    }
}
