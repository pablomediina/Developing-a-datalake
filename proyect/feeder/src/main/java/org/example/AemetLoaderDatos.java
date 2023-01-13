package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class AemetLoaderDatos {

    public String getData(String url, String apiKey) throws IOException {
        String accept = Jsoup.connect(getUrl(getDataUrl(url, apiKey)))
                .validateTLSCertificates(false)
                .timeout(6000)
                .ignoreContentType(true)
                .header("accept", "application/json")
                .method(Connection.Method.GET)
                .maxBodySize(0).execute().body();
        return accept;
    }

    public static String getDataUrl(String url, String apiKey) throws IOException {
        String response = Jsoup.connect(url)
                .validateTLSCertificates(false)
                .timeout(6000)
                .ignoreContentType(true)
                .header("accept", "application/json")
                .header("api_key", apiKey)
                .method(Connection.Method.GET)
                .maxBodySize(0).execute().body();
        return response;
    }

    private String getUrl(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        return jsonObject.get("datos").getAsString();
    }
}