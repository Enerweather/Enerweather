package org.ulpgc.dacd.enerweather.weatherFeeder;

import com.google.gson.Gson;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.accessors.AccessorImp;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.EventPublisher;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.Accessor;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.domain.model.Weather;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.persistence.MessagePublisher;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.accessors.FetchException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final Accessor feeder;
    private final List<String> cities;

    public Controller(String apiKey) {
        this.feeder = new AccessorImp(apiKey);
        this.cities = getAllCities();
    }

    public List<String> getAllCities() {
        return List.of(
                "Madrid",
                "Barcelona",
                "Valencia",
                "Seville",
                "Zaragoza",
                "Málaga",
                "Murcia",
                "Palma",
                "Las Palmas de Gran Canaria",
                "Bilbao",
                "Alicante",
                "Córdoba",
                "Valladolid",
                "Vigo",
                "Gijón",
                "Vitoria-Gasteiz",
                "La Coruña",
                "Granada",
                "Elche",
                "Oviedo",
                "Badalona",
                "Cartagena",
                "Terrassa",
                "Jerez de la Frontera",
                "Sabadell",
                "Móstoles",
                "Santa Cruz de Tenerife",
                "Alcalá de Henares"
        );
    }

    public void execute() {
        for (String city : cities) {
            try {
                Weather data = feeder.fetchCurrentWeather(city);
                if (data != null) {
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

    public void startPeriodicTask(long intervalInSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::execute, 0, intervalInSeconds, TimeUnit.SECONDS);
    }
}