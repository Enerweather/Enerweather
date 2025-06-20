package org.ulpgc.dacd.enerweather.businessunit.store;

import com.google.gson.JsonObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Map.entry;

public class CsvEventWriter {

    private static final Map<String, Integer> CITY_POPULATIONS = Map.ofEntries(
            entry("Madrid", 3422416),
            entry("Barcelona", 1686208),
            entry("Valencia", 824340),
            entry("Seville", 686741),
            entry("Zaragoza", 691037),
            entry("Málaga", 592346),
            entry("Murcia", 471982),
            entry("Palma", 438234),
            entry("Las Palmas de Gran Canaria", 383516),
            entry("Bilbao", 350000),
            entry("Alicante", 358608),
            entry("Córdoba", 326039),
            entry("Valladolid", 299265),
            entry("Vigo", 296692),
            entry("Gijón", 271717),
            entry("Vitoria-Gasteiz", 253996),
            entry("La Coruña", 247604),
            entry("Granada", 233648),
            entry("Elche", 234765),
            entry("Oviedo", 219910),
            entry("Badalona", 223166),
            entry("Cartagena", 216108),
            entry("Terrassa", 223627),
            entry("Jerez de la Frontera", 213105),
            entry("Sabadell", 216520),
            entry("Móstoles", 210309),
            entry("Santa Cruz de Tenerife", 209194),
            entry("Alcalá de Henares", 197562)
    );

    private final Path baseDir = Paths.get("datamart");

    public void handleEvent(String topicName, JsonObject event) {
        String timestamp    = event.get("timestamp").getAsString();
        String date  = LocalDate.parse(timestamp.substring(0,10))
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        Path dir     = baseDir.resolve(topicName);
        Path csvFile = dir.resolve(date + ".csv");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.out.println("Error creating directories");
            return;
        }


        if ("weather".equals(topicName)) {
            String[] headers = {"timestamp", "cityName", "description", "windSpeed"};

            boolean writeHeader = !Files.exists(csvFile);
            try (FileWriter fw = new FileWriter(csvFile.toFile(), true);
                 CSVPrinter printer = new CSVPrinter(fw,
                         CSVFormat.DEFAULT
                                 .withHeader(headers)
                                 .withSkipHeaderRecord(!writeHeader))) {

                Object[] vals = new Object[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    vals[i] = event.get(headers[i]).getAsString();
                }
                printer.printRecord(vals);
            } catch (IOException e) {
                System.out.println("Error writing to weather file");
            }
        }

        else if ("energy".equals(topicName)) {
            String[] headers = {"timestamp", "city", "indicator", "value"};

            double totalEnergy = Double.parseDouble(event.get("value").getAsString());
            String indicator = event.get("indicator").getAsString();

            int totalPop = CITY_POPULATIONS.values().stream().mapToInt(Integer::intValue).sum();

            boolean writeHeader = !Files.exists(csvFile);
            try (FileWriter fw = new FileWriter(csvFile.toFile(), true);
                 CSVPrinter printer = new CSVPrinter(fw,
                         CSVFormat.DEFAULT
                                 .withHeader(headers)
                                 .withSkipHeaderRecord(!writeHeader))) {

                for (Map.Entry<String, Integer> entry : CITY_POPULATIONS.entrySet()) {
                    String city = entry.getKey();
                    int pop = entry.getValue();
                    double share = (pop * totalEnergy) / totalPop;

                    printer.printRecord(
                            timestamp,
                            city,
                            indicator,
                            String.format("%.2f", share)
                    );
                }

            } catch (IOException e) {
                System.out.println("Error writing to energy file: " + e.getMessage());
            }
        }
    }
}