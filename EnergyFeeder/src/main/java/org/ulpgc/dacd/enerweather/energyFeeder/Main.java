package org.ulpgc.dacd.enerweather.energyFeeder;

public class Main {
    public static void main(String[] args) {
        String url = "https://apidatos.ree.es/en/datos/balance/balance-electrico";
        Controller controller = new Controller(url);
        controller.startPeriodicTask(86400);
    }
}
