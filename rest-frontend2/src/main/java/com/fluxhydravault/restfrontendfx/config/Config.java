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
    private String fileServerUri;

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

        Defaults.getDefaultConfig(this);
    }

    public void loadConfig() {
        Gson gson = new Gson();
        File configFile = new File(configLocation + "config.json");

        try {
            List<String> lines = Files.readAllLines(configFile.toPath());
            StringBuilder json = new StringBuilder();
            lines.forEach(json::append);
            Config tmp = gson.fromJson(json.toString(), Config.class);

            setCurrentAdmin(tmp.currentAdmin);
            setBaseUri(tmp.baseUri);
            setUserToken(tmp.userToken);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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

    public String getConfigLocation() {
        return configLocation;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getFileServerUri() {
        return fileServerUri;
    }

    public void setFileServerUri(String fileServerUri) {
        this.fileServerUri = fileServerUri;
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
