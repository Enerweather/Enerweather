package WeatherApi;

public class OWMFeeder implements WeatherFeeder{
    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/";

    public OWMFeeder(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public WeatherData fetchCurrentWeather(String location) {
        String url = baseUrl + "weather?q=" + location + "&appid=" + apiKey;
        WeatherData weatherData = null;
        return weatherData;
    }
}