package org.ulpgc.dacd.infrastructure.accessors;

public class REFetchException extends Exception {
    public REFetchException(String message) {
        super(message);
    }
    public REFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
