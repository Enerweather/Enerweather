package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.accessors;

public class ReeFetchException extends Exception {
    public ReeFetchException(String message) {
        super(message);
    }
    public ReeFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
