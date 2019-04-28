package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Token;

public interface TokenService {
    String generateUserToken(String playerID);

    Token getUserToken(String token);

    boolean isValidPlayerToken(String token);

    String generateAdminToken(String adminID);

    Token getAdminToken(String token);

    boolean isValidAdminToken(String token);
}
