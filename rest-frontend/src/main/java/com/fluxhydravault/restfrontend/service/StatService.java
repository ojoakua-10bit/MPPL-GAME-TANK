package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.google.gson.Gson;

public class StatService {
    private Gson gson;
    private Config config;

    private static final StatService instance = new StatService();

    private StatService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static StatService getInstance() {
        return instance;
    }
}
