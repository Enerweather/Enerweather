package org.ulpgc.dacd.enerweather.reFeeder;

import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.EnergyRepositoryPort;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.EnergyFeederInterface;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.accessors.EnergyAccessor;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence.EnergyDBInitializer;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence.EnergyRepository;

public class Main {
    public static void main(String[] args) {
        EnergyDBInitializer.createRETable();
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        EnergyFeederInterface feeder = new EnergyAccessor(reUrl);
        EnergyRepositoryPort repository = new EnergyRepository();
        EnergyController controller = new EnergyController(feeder, repository);

        controller.startPeriodicTask(3600);
    }
}
