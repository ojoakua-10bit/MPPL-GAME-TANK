package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Token;

public interface TokenService {
    String generateToken(String playerID);

    Token getToken(String token);

    boolean isValidToken(String token);
}
