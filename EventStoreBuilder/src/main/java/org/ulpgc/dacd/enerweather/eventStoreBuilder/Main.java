package org.ulpgc.dacd.enerweather.eventStoreBuilder;

public class Main {
    public static void main(String[] args) {
        try {
            EventStoreBuilder builder = new EventStoreBuilder();
            builder.start();
            System.out.println("EventStoreBuilder started. Hit Ctrl+C to exit.");
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Failed to start EventStoreBuilder: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
