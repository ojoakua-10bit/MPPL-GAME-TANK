package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.google.gson.Gson;

public class ItemService {
    private Gson gson;
    private Config config;

    private static final ItemService instance = new ItemService();

    private ItemService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static ItemService getInstance() {
        return instance;
    }
}
