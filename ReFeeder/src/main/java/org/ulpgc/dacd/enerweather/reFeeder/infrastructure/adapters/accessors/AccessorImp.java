package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.accessors;

import org.ulpgc.dacd.enerweather.reFeeder.application.domain.model.Energy;
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

public class AccessorImp implements org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.Accessor {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public AccessorImp(String baseUrl) {
        this(baseUrl, HttpClient.newHttpClient());
    }

    public AccessorImp(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    @Override
    public List<Energy> fetchEnergyData() throws FetchException {
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
                System.err.println("HTTP error body: " + response.body());
                throw new FetchException("Unexpected HTTP status: " + response.statusCode());
            }

            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            return parseRenovables(root, start);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FetchException("Failed to fetch energy data", e);
        }
    }

    private List<Energy> parseRenovables(JsonObject root, String requestStart) throws FetchException {
        JsonArray included = root.getAsJsonArray("included");
        if (included == null) {
            throw new FetchException("No 'included' array in response");
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
            throw new FetchException("No 'Renovable' group found");
        }

        JsonArray content = renovGroup.getAsJsonObject("attributes").getAsJsonArray("content");
        List<Energy> list = new ArrayList<>();

        for (JsonElement ce : content) {
            JsonObject item = ce.getAsJsonObject();
            JsonObject attrs = item.getAsJsonObject("attributes");
            JsonArray  valuesArr = attrs.getAsJsonArray("values");
            if (valuesArr == null || valuesArr.isEmpty()) continue;

            JsonObject v = valuesArr.get(0).getAsJsonObject();
            Energy data = new Energy(
                    attrs.get("title").getAsString(),
                    v.get("value").getAsDouble(),
                    requestStart
            );

            list.add(data);
        }

        return list;
    }
}