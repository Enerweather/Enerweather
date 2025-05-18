package org.ulpgc.dacd.enerweather.businessunit;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();

        Runtime.getRuntime().addShutdownHook(new Thread(controller::stop));

        System.out.println("EventStoreBuilder started. Use the menu to interact or press Ctrl+C to exit.");
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
