package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
import com.fluxhydravault.restfrontendfx.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
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
        gson = new Gson();
        config = Config.getConfig();
    }

    public static ItemService getInstance() {
        return instance;
    }

    public Item newItem() {
        return null;
    }

    public List<Item> getItemLists() {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
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
        catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }
}
