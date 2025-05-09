package org.ulpgc.dacd.infrastructure.api;

import org.ulpgc.dacd.application.port.REFeederInterface;
import org.ulpgc.dacd.domain.model.RE;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class REFeeder implements REFeederInterface {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public REFeeder(String baseUrl) {
        this(baseUrl, HttpClient.newHttpClient());
    }

    public REFeeder(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    @Override
    public List<RE> fetchEnergyData() throws REFetchException {
        try {
            LocalDate queryDate = LocalDate.now().minusDays(4);
            String start = queryDate.atStartOfDay().format(formatter);
            String end = queryDate.atTime(23, 59, 59).format(formatter);

            String url = String.format("%s?start_date=%s&end_date=%s&time_trunc=day",
                    baseUrl,
                    URLEncoder.encode(start, StandardCharsets.UTF_8),
                    URLEncoder.encode(end, StandardCharsets.UTF_8)
            );
            System.out.println("Fetching RE data from URL: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new REFetchException("Unexpected HTTP status: " + response.statusCode());
            }

            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            return parseRenovables(root, start);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new REFetchException("Failed to fetch energy data", e);
        }
    }

    private List<RE> parseRenovables(JsonObject root, String requestStart) throws REFetchException {
        JsonArray included = root.getAsJsonArray("included");
        if (included == null) {
            throw new REFetchException("No 'included' array in response");
        }

        JsonObject renovGroup = null;
        for (JsonElement e : included) {
            JsonObject grp = e.getAsJsonObject();
            if ("Renovable".equals(grp.get("type").getAsString())) {
                renovGroup = grp;
                break;
            }
        }
        if (renovGroup == null) {
            throw new REFetchException("No 'Renovable' group found");
        }

        JsonArray content = renovGroup.getAsJsonObject("attributes").getAsJsonArray("content");
        List<RE> list = new ArrayList<>();

        for (JsonElement ce : content) {
            JsonObject item = ce.getAsJsonObject();
            JsonObject attrs = item.getAsJsonObject("attributes");
            JsonArray  valuesArr = attrs.getAsJsonArray("values");
            if (valuesArr == null || valuesArr.isEmpty()) continue;

            JsonObject v = valuesArr.get(0).getAsJsonObject();
            RE data = new RE();
            data.setIndicator(attrs.get("title").getAsString());
            data.setValue(v.get("value").getAsDouble());
            data.setPercentage(v.get("percentage").getAsDouble());
            data.setTimestamp(requestStart);
            list.add(data);
        }

        return list;
    }
}