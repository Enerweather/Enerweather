import Weather.infrastructure.in.rest.WeatherController;
import Weather.domain.model.WeatherData;
import Weather.infrastructure.out.api.OWMFeeder;
import Weather.infrastructure.out.persistence.DBInitializer;
import Weather.infrastructure.out.persistence.WeatherRepository;

public class Main {
    public static void main(String[] args) {

        String apiKey = args[0];
        DBInitializer.createWeatherTable();

        OWMFeeder feeder = new OWMFeeder(apiKey);
        WeatherController weatherController = new WeatherController(feeder);
        WeatherRepository weatherRepository = new WeatherRepository();
        WeatherData weatherData = weatherController.getWeatherData("Madrid");

        if (weatherData != null) {
            weatherRepository.save(weatherData);
            System.out.println("Weather saved");
        }


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
