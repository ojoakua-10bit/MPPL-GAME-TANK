package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.google.gson.Gson;

public class ItemService {
    private Gson gson;
    private Config config;

    public ItemService() {
        gson = new Gson();
        config = Config.getConfig();
    }
}
