package com.fluxhydravault.restfrontendfx.service;

import com.fluxhydravault.restfrontendfx.ConnectionException;
import com.fluxhydravault.restfrontendfx.config.Config;
import com.fluxhydravault.restfrontendfx.config.Defaults;
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

public class StatService {
    private Gson gson;
    private Config config;

    private static StatService instance = new StatService();

    private StatService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    public static StatService getInstance() {
        return instance;
    }

    public Stat newStat(Stat stat) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(config.getBaseUri() + "/stats")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("stat_name", stat.getName())
                    .addParameter("stat_type", stat.getType().toString())
                    .addParameter("stat_value", Double.toString(stat.getValue()))
                    .build();
            System.out.println("Executing request " + request.getRequestLine());

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Stat.class).getType();
            StandardResponse<Stat> response = gson.fromJson(responseBody, responseType);

            return response.getData();
        } catch (HttpHostConnectException e) {
            throw new ConnectionException();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // throw some exception
            return null;
        }
    }

    public List<Stat> getStatLists() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(config.getBaseUri() + "/stats")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("n", "300")
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

    public Stat editStat(Stat stat) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request;
            StandardResponse<Stat> response;

            request = RequestBuilder.put()
                    .setUri(config.getBaseUri() + "/stats/" + stat.getStatId())
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("stat_name", stat.getName())
                    .addParameter("stat_type", stat.getType().toString())
                    .addParameter("stat_value", stat.getValue().toString())
                    .build();

            String responseBody = httpclient.execute(request, Defaults.getDefaultResponseHandler());
            Type responseType = TypeToken.getParameterized(StandardResponse.class, Stat.class).getType();
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

    public void deleteStat(Long statID) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(config.getBaseUri() + "/stats")
                    .addHeader("App-Token", Defaults.getAppToken())
                    .addHeader("User-Token", config.getUserToken())
                    .addParameter("stat_id", statID.toString())
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
