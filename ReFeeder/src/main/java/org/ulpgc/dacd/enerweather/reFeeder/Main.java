package org.ulpgc.dacd.enerweather.reFeeder;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence.DBInitializer;

public class Main {
    public static void main(String[] args) {
        String reUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        EnergyController controller = new EnergyController(reUrl);
        controller.startPeriodicTask(3600);
    }
}
