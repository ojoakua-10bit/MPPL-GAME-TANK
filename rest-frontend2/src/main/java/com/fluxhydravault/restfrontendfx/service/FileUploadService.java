package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.StandardResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketException;

public class FileUploadService {
    private Gson gson;
    private Config config;

    private static final FileUploadService instance = new FileUploadService();

    private FileUploadService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    public static FileUploadService getInstance() {
        return instance;
    }

    public String uploadAvatar(File avatar, String userID, boolean isAdmin) {
        String uri;
        if (isAdmin) {
            uri = config.getBaseUri() + "/uploads/images/admin/" + userID;
        }
        else {
            uri = config.getBaseUri() + "/uploads/images/" + userID;
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
            StandardResponse<String> response = gson.fromJson(responseBody, responseType);
            return response.getData();
        } catch (HttpHostConnectException e) {
            throw new ConnectionException();
        } catch (SocketException e) {
            throw new RuntimeException("Connection reset while uploading your avatar." +
                    "\nUsually caused by the file size exceeds server's limit (10MB).");
        }  catch (IOException e) {
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
            StandardResponse<String> response = gson.fromJson(responseBody, responseType);
            return response.getData();
        } catch (HttpHostConnectException e) {
            throw new ConnectionException();
        } catch (SocketException e) {
            throw new RuntimeException("Connection reset while uploading your file." +
                    "\nUsually caused by the file size exceeds server's limit (10MB).");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
