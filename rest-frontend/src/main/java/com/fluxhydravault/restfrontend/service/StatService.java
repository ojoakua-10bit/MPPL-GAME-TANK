package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.google.gson.Gson;

public class StatService {
    private Gson gson;
    private Config config;

    public StatService() {
        gson = new Gson();
        config = Config.getConfig();
    }
}
