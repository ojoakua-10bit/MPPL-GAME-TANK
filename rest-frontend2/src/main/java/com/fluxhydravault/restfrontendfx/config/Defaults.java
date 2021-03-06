package com.fluxhydravault.restfrontendfx.config;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class Defaults {
    private Defaults() { }

    private static final String IMAGE_LOCATION = "avatar.img";

    private static final String APP_TOKEN = "e6a065c4517d3520dbaa8b63fc25527caccc39ce6dda5026b5232c027053fb3b";

    static void getDefaultConfig(Config config) {
        config.setCurrentAdmin(null);
        config.setUserToken(null);
        config.setBaseUri("https://ojoakua.site/tankgame");
        config.setFileServerUri("https://ojoakua.site");
    }

    public static String getAppToken() {
        return APP_TOKEN;
    }

    public static String getImageLocation() {
        return IMAGE_LOCATION;
    }

    public static ResponseHandler<String> getDefaultResponseHandler() {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
            else {
                throw new ClientProtocolException(EntityUtils.toString(response.getEntity()));
            }
        };
    }
}
