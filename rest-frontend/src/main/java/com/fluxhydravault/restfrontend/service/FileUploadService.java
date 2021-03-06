package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.fluxhydravault.restfrontend.config.Defaults;
import com.fluxhydravault.restfrontend.model.StandardResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileUploadService {
    private Gson gson;
    private Config config;

    private static final FileUploadService instance = new FileUploadService();

    private FileUploadService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static FileUploadService getInstance() {
        return instance;
    }

    public String uploadAvatar(File avatar, String userID, boolean isAdmin) {
        String uri;
        if (isAdmin) {
            uri = config.getBaseUri() + "/uploads/images/" + userID;
        }
        else {
            uri = config.getBaseUri() + "/uploads/images/admin/" + userID;
        }

        HttpEntity data = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("image_data", avatar)
                .build();

        HttpUriRequest request = RequestBuilder.post()
                .setUri(uri)
                .addHeader("App-Token", Defaults.getAppToken())
                .addHeader("User-Token", config.getUserToken())
                .setEntity(data)
                .build();

        System.out.println("Executing request " + request.getRequestLine());

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, String.class).getType();
            return gson.fromJson(responseBody, responseType);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String uploadAsset(File asset, String itemID) {
        HttpEntity data = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("assets_data", asset)
                .build();

        HttpUriRequest request = RequestBuilder.post()
                .setUri(config.getBaseUri() + "/uploads/assets/" + itemID)
                .addHeader("App-Token", Defaults.getAppToken())
                .addHeader("User-Token", config.getUserToken())
                .setEntity(data)
                .build();

        System.out.println("Executing request " + request.getRequestLine());

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, String.class).getType();
            return gson.fromJson(responseBody, responseType);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
