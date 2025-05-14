import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.application.port.RERepositoryPort;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.application.port.REFeederInterface;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.application.port.GetERUseCase;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.application.service.REService;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.domain.model.RE;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.infrastructure.accessors.REFeeder;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.infrastructure.rest.REController;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.infrastructure.accessors.REFetchException;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.infrastructure.persistence.REDBInitializer;
import org.ulpgc.dacd.enerweather.weatherFeeder.enerweather.reFeeder.infrastructure.persistence.RERepository;

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
                        "%s = %.2f (%+.2f%%) at %s%n",
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
