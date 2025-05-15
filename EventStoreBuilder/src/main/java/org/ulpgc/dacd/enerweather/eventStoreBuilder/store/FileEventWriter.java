package org.ulpgc.dacd.enerweather.eventStoreBuilder.store;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileEventWriter {
    private static final Logger log = LoggerFactory.getLogger(FileEventWriter.class);
    private final Path baseDir = Paths.get("eventstore");

    public void handleEvent(String topicName, JsonObject event) {
        topicName = topicName.replaceAll("[^a-zA-Z0-9._-]", "_");

        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Path dir = baseDir.resolve(topicName);
        Path file = dir.resolve(date + ".events");

        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            log.error("Couldn't create directory {}: {}", dir, e.getMessage());
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(event.toString());
            writer.newLine();
        } catch (IOException e) {
            log.error("Error writing event in {}: {}", file, e.getMessage());
        }
    }
}
