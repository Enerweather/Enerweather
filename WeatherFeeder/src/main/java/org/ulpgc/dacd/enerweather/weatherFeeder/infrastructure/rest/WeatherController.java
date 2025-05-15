package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.rest;

import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.EventPublisher;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.WeatherRepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.service.WeatherService;
import org.ulpgc.dacd.enerweather.weatherFeeder.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.persistence.MessagePublisher;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherRepositoryPort repository;
    private final List<String> cities;

    public WeatherController(WeatherService getWeather, WeatherRepositoryPort repository, List<String> cities) {

        this.weatherService = getWeather;
        this.repository = repository;
        this.cities = cities;
    }

    public void execute() {
        for (String city : cities) {
            Weather data = weatherService.getWeatherFor(city);
            if (data != null) {
                repository.save(data);
                try (EventPublisher publisher = new MessagePublisher()) {
                    String json = new Gson().toJson(data);
                    publisher.publish(json);
                    System.out.println("Published: " + json);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Data is null");
            }
        }
    }

    public void startPeriodicTask(long intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::execute, 0, intervalSeconds, TimeUnit.SECONDS);
    }
}