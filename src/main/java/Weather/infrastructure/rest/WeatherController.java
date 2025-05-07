package Weather.infrastructure.rest;

import Weather.application.port.GetWeatherUseCase;
import Weather.domain.model.WeatherData;

public class WeatherController{
    private final GetWeatherUseCase getWeather;
    public WeatherController(GetWeatherUseCase getWeather) {
        this.getWeather = getWeather;
    }

    public WeatherData getWeatherData(String city){
        return getWeather.execute(city);
    }
}