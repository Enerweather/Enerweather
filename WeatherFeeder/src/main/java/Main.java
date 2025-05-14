import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.GetWeatherUseCase;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.WeatherFeeder;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.port.WeatherRepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.application.service.WeatherService;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.accessors.OWMaccessor;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.persistence.DBInitializer;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.persistence.WeatherRepository;
import org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.rest.WeatherController;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];

        DBInitializer.createWeatherTable();

        WeatherFeeder feeder = new OWMaccessor(apiKey);
        WeatherRepositoryPort repo = new WeatherRepository();
        GetWeatherUseCase service = new WeatherService(feeder);

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
        WeatherController controller = new WeatherController(service, repo, cities);
        controller.startPeriodicTask(3600);

        }


    }

