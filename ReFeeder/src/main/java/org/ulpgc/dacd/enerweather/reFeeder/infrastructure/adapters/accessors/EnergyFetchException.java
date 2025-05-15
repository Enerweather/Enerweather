package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.accessors;

public class EnergyFetchException extends Exception {
    public EnergyFetchException(String message) {
        super(message);
    }
    public EnergyFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
