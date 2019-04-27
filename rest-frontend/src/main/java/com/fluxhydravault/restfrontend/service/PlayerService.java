package com.fluxhydravault.restfrontend.service;

import com.fluxhydravault.restfrontend.config.Config;
import com.fluxhydravault.restfrontend.model.Player;
import com.fluxhydravault.restfrontend.model.SearchResponse;
import com.fluxhydravault.restfrontend.model.StandardResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PlayerService {
    private Gson gson;
    private Config config;

    public PlayerService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public Player newPlayer(String username, String password, String playerName) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/players")
                    .addHeader("App-Token", config.getAppToken())
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("player_name", playerName)
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, config.getResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Player.class).getType();
            StandardResponse<Player> response = gson.fromJson(responseBody, responseType);

            return response.getData();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public List<Player> getPlayerLists() {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/players")
                    .addHeader("App-Token", config.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, config.getResponseHandler());
            Type responseType = TypeToken.getParameterized(SearchResponse.class, Player.class).getType();
            SearchResponse<Player> response = gson.fromJson(responseBody, responseType);

            return response.getPossible_result();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public SearchResponse<Player> searchPlayer(String username) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/players?q=" + username)
                    .addHeader("App-Token", config.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, config.getResponseHandler());
            Type responseType = TypeToken.getParameterized(SearchResponse.class, Player.class).getType();

            return gson.fromJson(responseBody, responseType);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public Player searchPlayerById(String playerID) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/players/" + playerID)
                    .addHeader("App-Token", config.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, config.getResponseHandler());

            return gson.fromJson(responseBody, Player.class);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }
}
