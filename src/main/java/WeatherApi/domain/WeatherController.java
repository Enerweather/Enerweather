package WeatherApi.domain;

public class WeatherController{
    private final WeatherFeeder feeder;
    public WeatherController(WeatherFeeder feeder){
        this.feeder = feeder;
    }

    public WeatherData getWeatherData(String location){
        return feeder.fetchCurrentWeather(location);
    }
}