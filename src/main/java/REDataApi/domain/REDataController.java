package REDataApi.domain;

import REDataApi.infrastructure.api.REDataFeeder;
import REDataApi.infrastructure.api.REDataFetchException;
import java.util.List;


public class REDataController {
    private final REFeeder feeder;
    public REDataController() {
        this.feeder = new REDataFeeder(
                "https://apidatos.ree.es/en/datos/balance/balance-electrico"
        );
    }

    public REDataController(String baseUrl) {
        this.feeder = new REDataFeeder(baseUrl);
    }

    public List<REData> getEnergyData() throws REDataFetchException {
        return feeder.fetchEnergyData();
    }
}
