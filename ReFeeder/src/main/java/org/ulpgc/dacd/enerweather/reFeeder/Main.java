package org.ulpgc.dacd.enerweather.reFeeder;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence.EnergyDBInitializer;

public class Main {
    public static void main(String[] args) {
        EnergyDBInitializer.createRETable();
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        EnergyController controller = new EnergyController(reUrl);

        controller.startPeriodicTask(3600);
    }
}
