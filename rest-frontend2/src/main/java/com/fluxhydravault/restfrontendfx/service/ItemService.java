package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Item;
import com.fluxhydravault.restfrontendfx.model.StandardResponse;
import com.fluxhydravault.restfrontendfx.model.Stat;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ItemService {
    private Gson gson;
    private Config config;

    private static final ItemService instance = new ItemService();

    private ItemService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    public static ItemService getInstance() {
        return instance;
    }

    public Item newItem(Item item) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/items")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("item_category", item.getItemCategory().toString())
                    .addParameter("item_name", item.getItemName())
                    .addParameter("description", item.getDescription())
                    .addParameter("location", item.getModelLocation())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Item.class).getType();
            StandardResponse<Item> response = gson.fromJson(responseBody, responseType);

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

    public List<Item> getItemLists() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/items")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("n", "300")
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(List.class, Item.class).getType();
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

    public Item editItem(Item item) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request;
            StandardResponse<Item> response;

            request = RequestBuilder.put()
                    .setUri(config.getBaseUri() + "/items/" + item.getItemId())
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("name", item.getItemName())
                    .addParameter("item_category", item.getItemCategory().toString())
                    .addParameter("description", item.getDescription())
                    .addParameter("model_location", item.getModelLocation())
                    .build();

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Item.class).getType();
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

    public void deleteItem(String itemID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(config.getBaseUri() + "/items/" + itemID)
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

    public List<Stat> getItemStats(String itemID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/items/" + itemID + "/stats")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(List.class, Stat.class).getType();
            return gson.fromJson(responseBody, responseType);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public List<Stat> addItemStat(String itemID, Long statID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/items/" + itemID + "/stats")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("stat_id", statID.toString())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(List.class, Stat.class).getType();
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

    public List<Stat> deleteItemStats(String itemID, Long statID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(config.getBaseUri() + "/items/" + itemID + "/stats")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("stat_id", statID.toString())
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(List.class, Stat.class).getType();
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
}
