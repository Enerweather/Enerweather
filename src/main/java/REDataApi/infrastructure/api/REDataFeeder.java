package REDataApi.infrastructure.api;

import REDataApi.domain.REData;
import REDataApi.domain.REFeeder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class REDataFeeder implements REFeeder {
    private final String baseUrl = "https://apidatos.ree.es/en/datos/balance/balance-electrico";

    @Override
    public REData fetchEnergyData() {
        try{
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneDayAgoPlusOneHour = now.minusDays(1).plusHours(1);
            LocalDateTime oneDayAgo = now.minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            String startFormatted = oneDayAgo.format(formatter);
            String endFormatted = oneDayAgoPlusOneHour.format(formatter);

            String urlString = baseUrl + "?start_date=" + startFormatted + "&end_date=" + endFormatted + "&time_trunc=day";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject indicator = jsonResponse.getAsJsonArray("included").get(0).getAsJsonObject();
            String indicatorName = indicator.get("attributes").getAsJsonObject().get("title").getAsString();
            String unit = indicator.get("attributes").getAsJsonObject().get("unit").getAsString();
            JsonObject attributes = indicator.getAsJsonObject("attributes");
            JsonArray values = attributes.getAsJsonArray("values");

            JsonObject latestValue = values.get(values.size() - 1).getAsJsonObject();
            double value = latestValue.get("value").getAsDouble();
            String timestamp = latestValue.get("timestamp").getAsString();

            REData data = new REData();
            data.setIndicator(indicatorName);
            data.setUnit(unit);
            data.setValue(value);
            data.setTimestamp(timestamp);
            return data;


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
