package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.fluxhydravault.restfrontend.config.Defaults;
import com.fluxhydravault.restfrontend.model.Admin;
import com.fluxhydravault.restfrontend.model.TokenResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;

public class LoginService {
    private Gson gson;
    private Config config;

    private static final LoginService instance = new LoginService();

    private LoginService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static LoginService getInstance() {
        return instance;
    }

    public void login(String username, String password) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/auth/admin")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(TokenResponse.class, Admin.class).getType();
            TokenResponse<Admin> admin = gson.fromJson(responseBody, responseType);

            config.setUserToken(admin.getToken());
            config.setCurrentAdmin(admin.getData());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
