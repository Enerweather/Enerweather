package REDataApi;

public class REDataFeeder implements REFeeder {
    private String baseUrl = "https://www.ree.es/es/datos/apidatos";

    @Override
    public REData fetchEnergyData() {
        String url = baseUrl + "/indicadores";
        REData reData = new REData();
        return reData;
    }
}
