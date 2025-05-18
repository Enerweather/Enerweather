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

public class CsvEventWriter {
    private static final Logger log = LoggerFactory.getLogger(CsvEventWriter.class);

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

        String[] headers;
        if ("weather".equals(topicName)) {
            headers = new String[]{"timestamp","cityName","description","windSpeed"};
        } else if ("energy".equals(topicName)) {
            headers = new String[]{"timestamp","indicator","value"};
        } else {
            headers = event.keySet().toArray(String[]::new);
        }

        // 4) write or append
        boolean writeHeader = !Files.exists(csvFile);
        try (FileWriter fw = new FileWriter(csvFile.toFile(), true);
             CSVPrinter printer = new CSVPrinter(fw,
                     CSVFormat.DEFAULT
                             .withHeader(headers)
                             .withSkipHeaderRecord(!writeHeader)))
        {
            Object[] vals = new Object[headers.length];
            for (int i = 0; i < headers.length; i++) {
                vals[i] = event.get(headers[i]).getAsString();
            }
            printer.printRecord(vals);
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }
}