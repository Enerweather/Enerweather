package infrastructure.rest;

import application.port.GetWeatherUseCase;
import domain.model.Weather;

public class WeatherController{
    private final GetWeatherUseCase getWeather;
    public WeatherController(GetWeatherUseCase getWeather) {
        this.getWeather = getWeather;
    }

    public Weather getWeatherData(String city){
        return getWeather.execute(city);
    }
}