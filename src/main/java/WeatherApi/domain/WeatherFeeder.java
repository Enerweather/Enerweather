package WeatherApi.domain;

public interface WeatherFeeder {
    public WeatherData fetchCurrentWeather(String location);
}
