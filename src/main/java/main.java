import REDataApi.domain.REDataController;
import REDataApi.infrastructure.api.REDataFeeder;
import REDataApi.domain.REFeeder;
import REDataApi.domain.REData;
import WeatherApi.infrastructure.api.OWMFeeder;
import WeatherApi.domain.WeatherController;
import WeatherApi.domain.WeatherData;
import WeatherApi.domain.WeatherFeeder;
import WeatherApi.infrastructure.persistence.DBInitializer;
import WeatherApi.infrastructure.persistence.WeatherRepository;

public class main {
    public static void main(String[] args) {
        String apiKey  = "0b7105bf1dbbc10240a3fe41a53f2123";
        DBInitializer.createWeatherTable();

        OWMFeeder feeder = new OWMFeeder(apiKey);
        WeatherController weatherController = new WeatherController(feeder);
        WeatherRepository weatherRepository = new WeatherRepository();

        REFeeder reFeeder = new REDataFeeder();
        REDataController reDataController = new REDataController(reFeeder);

        REData reData = reDataController.getEnergyData();
        if(reData != null) {
            System.out.println("Indicador :" + reData.getIndicator());
            System.out.println("Time: " + reData.getTimestamp());
        }
    }
}
