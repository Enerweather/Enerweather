import Weather.application.port.in.GetWeatherUseCase;
import Weather.application.port.out.WeatherFeeder;
import Weather.application.port.out.WeatherRepositoryPort;
import Weather.application.service.WeatherService;
import Weather.domain.model.WeatherData;
import Weather.infrastructure.out.api.OWMFeeder;
import Weather.infrastructure.out.persistence.DBInitializer;
import Weather.infrastructure.out.persistence.WeatherRepository;

public class Main {
    public static void main(String[] args) {

        String apiKey = args[0];
        DBInitializer.createWeatherTable();

        WeatherFeeder feeder = new OWMFeeder(apiKey);
        WeatherRepositoryPort repo = new WeatherRepository();

        GetWeatherUseCase service = new WeatherService(feeder, repo);

        String city = "London";
        WeatherData data = service.execute(city);
        System.out.println(
                "Weather in " + data.getCityName() + ": " + data.getTemperature());

       /* REDataController reDataController = new REDataController();

        try {
            List<REData> dataList = reDataController.getEnergyData();
            for (REData d : dataList) {
                System.out.printf("%s: value=%.2f, percentage=%.2f%%, date=%s%n",
                        d.getIndicator(), d.getValue(), d.getPercentage() * 100, d.getTimestamp());
            }
        } catch (REDataFetchException e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }

        */
    }
}
