import RE.application.port.REDataRepositoryPort;
import RE.application.port.REFeeder;
import RE.application.port.GetERDataUseCase;
import RE.application.service.REDataService;
import RE.domain.model.REData;
import RE.infrastructure.rest.REController;
import RE.infrastructure.api.REDataFeeder;
import RE.infrastructure.api.REDataFetchException;
import RE.infrastructure.persistence.REDBInitializer;
import RE.infrastructure.persistence.REDataRepository;

import java.util.List;
public class Main {
    public static void main(String[] args) {
        REDBInitializer.createRETable();
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        REFeeder reFeeder = new REDataFeeder(reUrl);
        REDataRepositoryPort reRepo = new REDataRepository();
        GetERDataUseCase reUc = new REDataService(reFeeder, reRepo);
        REController reCtrl = new REController(reUc);

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
