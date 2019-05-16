package com.fluxhydravault.restfrontendfx;

public class ConnectionException extends RuntimeException {
    public ConnectionException() {
        super("A connection problem has occurred.");
    }

    public ConnectionException(String message) {
        super(message);
    }
}
