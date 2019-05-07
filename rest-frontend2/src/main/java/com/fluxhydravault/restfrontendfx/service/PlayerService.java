package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Player;
import com.fluxhydravault.restfrontendfx.model.SearchResponse;
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

public class PlayerService {
    private Gson gson;
    private Config config;

    private static final PlayerService instance = new PlayerService();

    private PlayerService() {
        gson = new Gson();
        config = Config.getConfig();
    }

    public static PlayerService getInstance() {
        return instance;
    }

    public Player newPlayer(String username, String password, String playerName) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/players")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("player_name", playerName)
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
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
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(SearchResponse.class, Player.class).getType();
            SearchResponse<Player> response = gson.fromJson(responseBody, responseType);

            return response.getPossible_results();
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
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
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
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());

            return gson.fromJson(responseBody, Player.class);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public Player editPlayer(String password, int credit, boolean banStatus) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("credit_balance", Integer.toString(credit)));
        if (password != null)
            params.add(new BasicNameValuePair("password", password));

        try {
            HttpUriRequest request;
            StandardResponse<Player> response;

            request = RequestBuilder.patch()
                    .setUri(config.getBaseUri() + "/admins/" + config.getCurrentAdmin().getAdmin_id())
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameters(params.toArray(new NameValuePair[0]))
                    .build();

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Player.class).getType();
            response = gson.fromJson(responseBody, responseType);

            return response.getData();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }
}
