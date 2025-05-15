package org.ulpgc.dacd.enerweather.weatherFeeder;

import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.WeatherFeeder;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port.WeatherRepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.accessors.Accessor;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.persistence.DBInitializer;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.adapters.persistence.WeatherRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];

        DBInitializer.createWeatherTable();

        WeatherFeeder feeder = new Accessor(apiKey);
        WeatherRepositoryPort repo = new WeatherRepository();

        List<String> cities = List.of(
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
                "Hospitalet de Llobregat",
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
        WeatherController controller = new WeatherController(feeder, repo, cities);
        controller.startPeriodicTask(3600);

        }


    }

