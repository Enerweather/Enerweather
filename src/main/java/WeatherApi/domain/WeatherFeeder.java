package WeatherApi.domain;

public interface WeatherFeeder {
    WeatherData fetchCurrentWeather(String location);
}
