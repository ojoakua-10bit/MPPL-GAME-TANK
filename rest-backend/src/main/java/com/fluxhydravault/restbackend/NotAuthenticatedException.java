package com.fluxhydravault.restbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("You have no privileges to access this page.");
    }

    public NotAuthenticatedException(String s) {
        super(s);
    }
}
