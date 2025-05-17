package org.ulpgc.dacd.enerweather.weatherFeeder;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];
        Controller controller = new Controller(apiKey);
        controller.startPeriodicTask(3600);
        }
    }

