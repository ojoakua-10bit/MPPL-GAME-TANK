package com.fluxhydravault.restfrontend.config;

import com.fluxhydravault.restfrontend.model.Admin;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class Config {
    private final String APP_TOKEN = "e6a065c4517d3520dbaa8b63fc25527caccc39ce6dda5026b5232c027053fb3b";
    private final String configLocation = System.getenv("HOME") + "/.tank_game/";
    private final String configLocationWindows = System.getenv("HOMEDRIVE")
            + System.getenv("HOMEPATH") + "\\.tank_game\\";

    private Admin currentAdmin;
    private String userToken;
    private String baseUri;
    private ResponseHandler<String> responseHandler;

    private static final Config config = new Config();

    private Config() {
        responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
            else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        loadDefaultConfig();
    }

    private void loadDefaultConfig() {
        currentAdmin = null;
        userToken = null;
        baseUri = "http://localhost:7169";
    }

    public void saveConfig() { }

    public static Config getConfig() {
        return config;
    }

    public String getAppToken() {
        return APP_TOKEN;
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

    public ResponseHandler<String> getResponseHandler() {
        return responseHandler;
    }
}
