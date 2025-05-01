package Weather.infrastructure.in.rest;

import Weather.application.port.out.WeatherFeeder;
import Weather.domain.model.WeatherData;

public class WeatherController{
    private final WeatherFeeder feeder;
    public WeatherController(WeatherFeeder feeder){
        this.feeder = feeder;
    }

    public WeatherData getWeatherData(String location){
        return feeder.fetchCurrentWeather(location);
    }
}