package org.ulpgc.dacd.enerweather.weatherFeeder;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];
        WeatherController controller = new WeatherController(apiKey);
        controller.startPeriodicTask(3600);
        }
    }

