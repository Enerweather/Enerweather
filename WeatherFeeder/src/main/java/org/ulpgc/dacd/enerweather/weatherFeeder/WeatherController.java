package org.ulpgc.dacd.enerweather.weatherFeeder;

import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.EventPublisher;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.WeatherFeeder;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.WeatherRepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.persistence.MessagePublisher;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.accessors.FetchException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherController {
    private final WeatherFeeder feeder;
    private final WeatherRepositoryPort repository;
    private final List<String> cities;

    public WeatherController(WeatherFeeder feeder, WeatherRepositoryPort repository, List<String> cities) {

        this.feeder = feeder;
        this.repository = repository;
        this.cities = cities;
    }

    public void execute() {
        for (String city : cities) {
            try {
                Weather data = feeder.fetchCurrentWeather(city);
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
            } catch (FetchException e) {
                System.out.println("Error fetching weather for " + city);
            }
        }
    }

    public void startPeriodicTask(long intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::execute, 0, intervalSeconds, TimeUnit.SECONDS);
    }
}