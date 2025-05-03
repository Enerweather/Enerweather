import RE.application.port.out.REDataRepositoryPort;
import RE.application.port.out.REFeeder;
import RE.application.port.in.GetERDataUseCase;
import RE.application.service.REDataService;
import RE.domain.model.REData;
import RE.infrastructure.in.rest.REController;
import RE.infrastructure.out.api.REDataFeeder;
import RE.infrastructure.out.api.REDataFetchException;
import RE.infrastructure.out.persistence.REDBInitializer;
import RE.infrastructure.out.persistence.REDataRepository;
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

        REDBInitializer.createRETable();
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        REFeeder reFeeder     = new REDataFeeder(reUrl);
        REDataRepositoryPort reRepo = new REDataRepository();
        GetERDataUseCase reUc = new REDataService(reFeeder, reRepo);
        REController reCtrl   = new REController(reUc);

        List<REData> reList;
        try {
            reList = reCtrl.getEnergyData();
            System.out.println("Fetched " + reList.size());
            for (REData d : reList) {
                System.out.printf(
                        "  • %s = %.2f (%+.2f%%) at %s%n",
                        d.getIndicator(),
                        d.getValue(),
                        d.getPercentage() * 100,
                        d.getTimestamp()
                );
            }
        } catch (REDataFetchException ex) {
            System.err.println("RE error: " + ex.getMessage());
        }

    }
}
