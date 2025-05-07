package infrastructure.accessors;

import domain.model.Weather;
import application.port.WeatherFeeder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

public class OWMFeeder implements WeatherFeeder {
    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/";

    public OWMFeeder(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Weather fetchCurrentWeather(String location) {
        try{
            String urlString = baseUrl + "weather?q=" + URLEncoder.encode(location, "UTF-8") + "&appid=" + apiKey + "&units=metric";
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");

            JsonObject json = JsonParser
                    .parseReader(new InputStreamReader(conn.getInputStream()))
                    .getAsJsonObject();

            JsonObject main = json.getAsJsonObject("main");
            JsonObject wind = json.getAsJsonObject("wind");
            JsonObject weather = json.getAsJsonArray("weather").get(0).getAsJsonObject();

            Weather weatherData = new Weather();
            weatherData.setTemperature(main.get("temp").getAsDouble());
            weatherData.setHumidity(main.get("humidity").getAsInt());
            weatherData.setPressure(main.get("pressure").getAsInt());
            weatherData.setWindSpeed(wind.get("speed").getAsDouble());
            weatherData.setDescription(weather.get("description").getAsString());
            weatherData.setCityName(json.get("name").getAsString());
            return weatherData;
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            return null;
        }

    }
}