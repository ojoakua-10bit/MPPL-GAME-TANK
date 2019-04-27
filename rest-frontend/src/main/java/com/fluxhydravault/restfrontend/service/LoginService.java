package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.fluxhydravault.restfrontend.model.Admin;
import com.fluxhydravault.restfrontend.model.TokenResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public class LoginService {
    private Gson gson;
    private Config config;

    public LoginService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public void login(String username, String password) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/auth/admin")
                    .addHeader("App-Token", config.getAppToken())
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                }
                else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(request, responseHandler);
            System.out.println("----------------------------------------");

            Type responseType = new TypeToken<TokenResponse<Admin>>(){}.getType();
            TokenResponse<Admin> admin = gson.fromJson(responseBody, responseType);

            config.setUserToken(admin.getToken());
            config.setCurrentAdmin(admin.getData());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
