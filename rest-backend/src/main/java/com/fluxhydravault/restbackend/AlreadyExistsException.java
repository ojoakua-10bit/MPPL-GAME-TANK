package com.fluxhydravault.restbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String attribute) {
        super("Attribute '" + attribute + "' already used. Please pick another " + attribute +'.');
    }
}
