import application.port.RERepositoryPort;
import application.port.REFeederInterface;
import application.port.GetERUseCase;
import application.service.REService;
import domain.model.RE;
import infrastructure.api.REFeeder;
import infrastructure.rest.REController;
import infrastructure.api.REFetchException;
import infrastructure.persistence.REDBInitializer;
import infrastructure.persistence.RERepository;

import java.util.List;
public class Main {
    public static void main(String[] args) {
        REDBInitializer.createRETable();
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        REFeederInterface reFeeder = new REFeeder(reUrl);
        RERepositoryPort reRepo = new RERepository();
        GetERUseCase reUc = new REService(reFeeder, reRepo);
        REController reCtrl = new REController(reUc);

        List<RE> reList;
        try {
            reList = reCtrl.getEnergyData();
            System.out.println("Fetched " + reList.size());
            for (RE d : reList) {
                System.out.printf(
                        "  • %s = %.2f (%+.2f%%) at %s%n",
                        d.getIndicator(),
                        d.getValue(),
                        d.getPercentage() * 100,
                        d.getTimestamp()
                );
            }
        } catch (REFetchException ex) {
            System.err.println("RE error: " + ex.getMessage());
        }
    }
}
