package com.fluxhydravault.restfrontendfx;

public class UnexpectedResponse extends RuntimeException {
    public UnexpectedResponse() {
        super("The server sent an unexpected response.");
    }

    public UnexpectedResponse(String s) {
        super(s);
    }
}
