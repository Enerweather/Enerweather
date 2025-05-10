package org.ulpgc.dacd.store;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileEventWriter {
    private final Path baseDir = Paths.get("eventstore");

    public void handleEvent(String topicName, JsonObject event) {
        String bucket = hourBucket();
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Path dir = baseDir.resolve(topicName).resolve(bucket);
        Path file = dir.resolve(date + ".events");

        try {
            Files.createDirectories(dir);

            try (BufferedWriter writer =
                    Files.newBufferedWriter(
                            file,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND)) {
                writer.write(event.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String hourBucket() {
        int hour = LocalDateTime.now().getHour();
        return String.valueOf(hour * 3600);
    }
}
