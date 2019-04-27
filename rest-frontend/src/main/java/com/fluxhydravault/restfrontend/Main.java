package com.fluxhydravault.restfrontend;

import com.fluxhydravault.restfrontend.service.LoginService;

public class Main {
    public static void main(String[] args) {
        LoginService loginService = new LoginService();
        loginService.login("username", "password");
    }
}
