import RE.application.port.out.REFeeder;
import RE.application.port.in.GetERDataUseCase;
import RE.application.service.REDataService;
import RE.domain.model.REData;
import RE.infrastructure.in.rest.REController;
import RE.infrastructure.out.api.REDataFeeder;
import RE.infrastructure.out.api.REDataFetchException;
import Weather.application.port.in.GetWeatherUseCase;
import Weather.application.port.out.WeatherFeeder;
import Weather.application.port.out.WeatherRepositoryPort;
import Weather.application.service.WeatherService;
import Weather.domain.model.WeatherData;
import Weather.infrastructure.out.api.OWMFeeder;
import Weather.infrastructure.out.persistence.DBInitializer;
import Weather.infrastructure.out.persistence.WeatherRepository;

import java.util.List;

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

        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        REFeeder reFeeder     = new REDataFeeder(reUrl);
        GetERDataUseCase reUc = new REDataService(reFeeder);
        REController reCtrl   = new REController(reUc);

        try {
            List<REData> list = reCtrl.getEnergyData();
            System.out.println("Fetched " + list.size() + " RE entries");
        } catch (REDataFetchException ex) {
            System.err.println("RE error: " + ex.getMessage());
        }

    }
}
