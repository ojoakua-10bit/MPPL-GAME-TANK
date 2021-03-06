package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Player;
import com.fluxhydravault.restfrontendfx.model.SearchResponse;
import com.fluxhydravault.restfrontendfx.model.StandardResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpHostConnectException;
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
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    public static PlayerService getInstance() {
        return instance;
    }

    public List<Player> getPlayerLists() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/players")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(SearchResponse.class, Player.class).getType();
            SearchResponse<Player> response = gson.fromJson(responseBody, responseType);

            return response.getPossibleResults();
        }
        catch (HttpHostConnectException e) {
            throw new ConnectionException();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public SearchResponse<Player> searchPlayer(String username) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
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
        catch (HttpHostConnectException e) {
            throw new ConnectionException();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public Player searchPlayerById(String playerID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/players/" + playerID)
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());

            return gson.fromJson(responseBody, Player.class);
        }
        catch (HttpHostConnectException e) {
            throw new ConnectionException();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public Player editPlayer(String playerID, String password, int credit, boolean banStatus) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("credit_balance", Integer.toString(credit)));
        if (password != null)
            params.add(new BasicNameValuePair("password", password));

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request, request2;
            StandardResponse<Player> response;

            request2 = RequestBuilder.patch()
                    .setUri(config.getBaseUri() + "/players/" + playerID + "/ban")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("value", Boolean.toString(banStatus))
                    .build();

            request = RequestBuilder.patch()
                    .setUri(config.getBaseUri() + "/players/" + playerID)
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameters(params.toArray(new NameValuePair[0]))
                    .build();

            httpclient.execute(request2, Defaults.getDefaultResponseHandler());
            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Player.class).getType();
            response = gson.fromJson(responseBody, responseType);

            return response.getData();
        }
        catch (HttpHostConnectException e) {
            throw new ConnectionException();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public void deletePlayer(String playerID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(config.getBaseUri() + "/players/" + playerID)
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            httpclient.execute(request, Defaults.getDefaultResponseHandler());
        }
        catch (HttpHostConnectException e) {
            throw new ConnectionException();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
        }
    }
}
