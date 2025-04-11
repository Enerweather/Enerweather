package WeatherApi.infrastructure.api;

import WeatherApi.domain.WeatherData;
import WeatherApi.domain.WeatherFeeder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OWMFeeder implements WeatherFeeder {
    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/";

    public OWMFeeder(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public WeatherData fetchCurrentWeather(String location) {
        try{
            String urlString = baseUrl + "weather?q=" + URLEncoder.encode(location, "UTF-8") + "&appid=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject main = jsonResponse.getAsJsonObject("main");
            JsonObject wind = jsonResponse.getAsJsonObject("wind");
            JsonObject weather = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject();

            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(main.get("temp").getAsDouble());
            weatherData.setHumidity(main.get("humidity").getAsInt());
            weatherData.setPressure(main.get("pressure").getAsInt());
            weatherData.setWindSpeed(wind.get("speed").getAsDouble());
            weatherData.setDescription(weather.get("description").getAsString());
            weatherData.setCityName(jsonResponse.get("name").getAsString());
            return weatherData;
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            return null;
        }

    }
}