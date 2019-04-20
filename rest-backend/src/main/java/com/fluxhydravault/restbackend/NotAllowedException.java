package com.fluxhydravault.restbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class NotAllowedException extends RuntimeException {
    public NotAllowedException() {
        super("This method is possible, but disabled due to security reason.");
    }

    public NotAllowedException(String s) {
        super(s);
    }
}
