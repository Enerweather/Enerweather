import REDataApi.REDataController;
import REDataApi.REDataFeeder;
import REDataApi.REFeeder;
import REDataApi.REData;
import WeatherApi.OWMFeeder;
import WeatherApi.WeatherController;
import WeatherApi.WeatherData;
import WeatherApi.WeatherFeeder;

public class main {
    public static void main(String[] args) {
        WeatherFeeder weatherFeeder = new OWMFeeder("0b7105bf1dbbc10240a3fe41a53f2123");
        WeatherController weatherController = new WeatherController(weatherFeeder);

        WeatherData weatherData = weatherController.getWeatherData("Las Palmas");
        System.out.println("clima:");
        System.out.println("temp:" + weatherData.getTemperature());
        System.out.println("humidity:" + weatherData.getHumidity());


        REFeeder reFeeder = new REDataFeeder();
        REDataController reDataController = new REDataController(reFeeder);

        REData reData = reDataController.getEnergyData();
        if(reData != null) {
            System.out.println("Indicador :" + reData.getIndicator());
            System.out.println("Time: " + reData.getTimestamp());
        }
    }
}
