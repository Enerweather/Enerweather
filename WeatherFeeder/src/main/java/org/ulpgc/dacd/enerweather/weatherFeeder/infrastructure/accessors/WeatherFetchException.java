package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.accessors;

public class WeatherFetchException extends Exception {
    public WeatherFetchException(String message) {
        super(message);
    }
    public WeatherFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
