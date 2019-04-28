package com.fluxhydravault.restbackend.utils;

import com.fluxhydravault.restbackend.NotAuthenticatedException;
import com.fluxhydravault.restbackend.services.TokenService;

import java.util.LinkedHashMap;

public class HeaderChecker {
    private HeaderChecker() { }

    private static LinkedHashMap<String, String> appTokenList;

    static {
        appTokenList = new LinkedHashMap<>();
        appTokenList.put("PLAYER", "5430eeed859cad61d925097ec4f532461ccf1ab6b9802b09a313be1478a4d614");
        appTokenList.put("ADMIN", "e6a065c4517d3520dbaa8b63fc25527caccc39ce6dda5026b5232c027053fb3b");
    }

    public static void checkHeader(String appToken, String userToken, String mode, TokenService tokenService) {
        if (appToken == null) {
            throw new NotAuthenticatedException();
        }

        if (!mode.equals("BOTH") && !appToken.equals(appTokenList.get(mode))) {
            throw new NotAuthenticatedException();
        }

        if (userToken == null) {
            throw new NotAuthenticatedException("Unauthorized user detected!");
        }

        if (mode.equals("PLAYER") && !tokenService.isValidPlayerToken(userToken)) {
            throw new NotAuthenticatedException("This token has been expired.");
        }

        if (mode.equals("ADMIN") && !tokenService.isValidAdminToken(userToken)) {
            throw new NotAuthenticatedException("This token has been expired.");
        }

        if (mode.equals("BOTH") && (!tokenService.isValidAdminToken(userToken)
                && !tokenService.isValidPlayerToken(userToken))) {
            throw new NotAuthenticatedException("This token has been expired.");
        }
    }

    public static void checkAppToken(String appToken) {
        if (!appTokenList.containsValue(appToken)) {
            throw new NotAuthenticatedException("Unauthorized app detected!");
        }
    }
}
