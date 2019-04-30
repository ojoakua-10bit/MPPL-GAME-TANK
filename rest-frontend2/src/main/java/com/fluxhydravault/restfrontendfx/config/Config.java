package com.fluxhydravault.restfrontendfx.config;

import com.fluxhydravault.restfrontendfx.model.Admin;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Config {
    private final String configLocation;

    private Admin currentAdmin;
    private String userToken;
    private String baseUri;

    private static final Config config = new Config();

    private Config() {
        String os = System.getProperty("os.name");

        if (os.startsWith("Windows")) {
            configLocation = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH") + "\\.tank_game\\";
        }
        else {
            configLocation = System.getenv("HOME") + "/.tank_game/";
        }

        try {
            Files.createDirectory(Paths.get(configLocation));
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }

        if (!loadConfig()) {
            Defaults.getDefaultConfig(this);
        }
    }

    private boolean loadConfig() {
        Gson gson = new Gson();
        File configFile = new File(configLocation + "config.json");

        if (!configFile.exists() && configFile.isDirectory()) {
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(configFile.toPath());
            StringBuilder json = new StringBuilder();
            lines.forEach(json::append);
            Config tmp = gson.fromJson(json.toString(), Config.class);

            setCurrentAdmin(tmp.currentAdmin);
            setBaseUri(tmp.baseUri);
            setUserToken(tmp.userToken);

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void saveConfig() {
        Gson gson = new Gson();
        File configFile = new File(configLocation + "config.json");

        if (configFile.isDirectory()) {
            try {
                Files.delete(configFile.toPath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(gson.toJson(this));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Config getConfig() {
        return config;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
