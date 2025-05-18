package org.ulpgc.dacd.enerweather.businessunit;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();

        // add a shutdown hook to clean up
        Runtime.getRuntime().addShutdownHook(new Thread(controller::stop));

        // keep running until Ctrl+C
        System.out.println("EventStoreBuilder started. Press Ctrl+C to exit.");
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
