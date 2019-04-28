package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.google.gson.Gson;

public class AdminService {
    private Gson gson;
    private Config config;

    private static final AdminService instance = new AdminService();

    private AdminService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static AdminService getInstance() {
        return instance;
    }
}
