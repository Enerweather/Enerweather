package org.ulpgc.dacd.enerweather.reFeeder;

import org.ulpgc.dacd.enerweather.reFeeder.application.port.ReeRepositoryPort;
import org.ulpgc.dacd.enerweather.reFeeder.application.port.ReeFeederInterface;
import org.ulpgc.dacd.enerweather.reFeeder.application.service.ReeService;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.accessors.ReeAccessor;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.rest.ReeController;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.persistence.ReeDBInitializer;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.persistence.ReeRepository;

public class Main {
    public static void main(String[] args) {
        ReeDBInitializer.createRETable();
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        ReeFeederInterface reFeeder = new ReeAccessor(reUrl);
        ReeRepositoryPort reRepo = new ReeRepository();
        ReeService service = new ReeService(reFeeder);
        ReeController controller = new ReeController(service, reRepo);

        controller.startPeriodicTask(3600);
    }
}
