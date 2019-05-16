package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.UnauthorizedException;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Admin;
import com.fluxhydravault.restfrontendfx.model.ErrorResponse;
import com.fluxhydravault.restfrontendfx.model.TokenResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;

public class LoginService {
    private Gson gson;
    private Config config;

    private static final LoginService instance = new LoginService();

    private LoginService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    public static LoginService getInstance() {
        return instance;
    }

    public void login(String username, String password) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
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
            config.saveConfig();
        }
        catch (HttpHostConnectException e) {
            throw new ConnectionException();
        }
        catch (ClientProtocolException e) {
            ErrorResponse response = gson.fromJson(e.getMessage(), ErrorResponse.class);
            if (response.getStatus() == 401) {
                throw new UnauthorizedException(response.getMessage());
            }
            else {
                throw new RuntimeException(response.getFormattedMessage());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
