import org.ulpgc.dacd.application.port.GetWeatherUseCase;
import org.ulpgc.dacd.application.port.WeatherFeeder;
import org.ulpgc.dacd.application.port.WeatherRepositoryPort;
import org.ulpgc.dacd.application.service.WeatherService;
import org.ulpgc.dacd.domain.model.Weather;
import org.ulpgc.dacd.infrastructure.accessors.OWMFeeder;
import org.ulpgc.dacd.infrastructure.persistence.DBInitializer;
import org.ulpgc.dacd.infrastructure.persistence.WeatherRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];

        DBInitializer.createWeatherTable();

        WeatherFeeder feeder = new OWMFeeder(apiKey);
        WeatherRepositoryPort repo = new WeatherRepository();
        GetWeatherUseCase service = new WeatherService(feeder, repo);

        List<String> cities = List.of(
                "Madrid",
                "Barcelona",
                "Valencia",
                "Seville",
                "Zaragoza",
                "Málaga",
                "Murcia",
                "Palma",
                "Las Palmas de Gran Canaria",
                "Bilbao",
                "Alicante",
                "Córdoba",
                "Valladolid",
                "Vigo",
                "Gijón",
                "Hospitalet de Llobregat",
                "Vitoria-Gasteiz",
                "La Coruña",
                "Granada",
                "Elche",
                "Oviedo",
                "Badalona",
                "Cartagena",
                "Terrassa",
                "Jerez de la Frontera",
                "Sabadell",
                "Móstoles",
                "Santa Cruz de Tenerife",
                "Alcalá de Henares"
        );
        for (String city : cities) {
            Weather data = service.execute(city);
            if (data != null) {
                System.out.println(
                        "Weather in " + data.getCityName() + ": " + data.getTemperature());
            } else {
                System.out.println("Failed to get data for " + city);
            }

        }


    }

}
