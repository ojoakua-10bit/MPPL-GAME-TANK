package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Admin;
import com.fluxhydravault.restfrontendfx.model.StandardResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private Gson gson;
    private Config config;

    private static final AdminService instance = new AdminService();

    private AdminService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static AdminService getInstance() {
        return instance;
    }

    public Admin registerAdmin(String username, String password, String adminName) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/admins")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("admin_name", adminName)
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Admin.class).getType();
            StandardResponse<Admin> response = gson.fromJson(responseBody, responseType);

            return response.getData();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public Admin editAdmin(String username, String password, String adminName) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<NameValuePair> params = new ArrayList<>();
        if (username != null)
            params.add(new BasicNameValuePair("username", username));
        if (password != null)
            params.add(new BasicNameValuePair("password", password));
        if (adminName != null)
            params.add(new BasicNameValuePair("admin_name", adminName));

        try {
            HttpUriRequest request;
            StandardResponse<Admin> response;

            request = RequestBuilder.patch()
                    .setUri(config.getBaseUri() + "/admins/" + config.getCurrentAdmin().getAdmin_id())
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameters(params.toArray(new NameValuePair[0]))
                    .build();

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Admin.class).getType();
            response = gson.fromJson(responseBody, responseType);

            return response.getData();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public void deleteAdmin() {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/admins/" + config.getCurrentAdmin().getAdmin_id())
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            httpclient.execute(request, Defaults.getDefaultResponseHandler());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
        }
    }
}
