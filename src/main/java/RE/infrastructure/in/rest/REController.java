package RE.infrastructure.in.rest;

import RE.application.port.out.REFeeder;
import RE.domain.model.REData;
import RE.infrastructure.out.api.REDataFeeder;
import RE.infrastructure.out.api.REDataFetchException;
import java.util.List;


public class REController {
    private final REFeeder feeder;
    public REController() {
        this.feeder = new REDataFeeder(
                "https://apidatos.ree.es/en/datos/balance/balance-electrico"
        );
    }

    public REController(String baseUrl) {
        this.feeder = new REDataFeeder(baseUrl);
    }

    public List<REData> getEnergyData() throws REDataFetchException {
        return feeder.fetchEnergyData();
    }
}
