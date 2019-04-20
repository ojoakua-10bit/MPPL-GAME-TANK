package com.fluxhydravault.restbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoSuchPrivilegeException extends RuntimeException {
    public NoSuchPrivilegeException() {
        super("You don't have such privilege to do that.");
    }
}
