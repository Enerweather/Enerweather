package org.ulpgc.dacd.enerweather.eventStoreBuilder.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonMessageListener implements MessageListener {
    private final FileEventWriter writer = new FileEventWriter();
    private boolean repeatedLine = false;
    @Override
    public void onMessage(Message message) {
        try {
            repeatedLine = false;
            Gson gson = new Gson();
            if (!(message instanceof TextMessage)) return;
            String json = ((TextMessage) message).getText();
            JsonObject event = gson.fromJson(json, JsonObject.class);
            String topicName = message.getJMSDestination().toString().substring(8);

            String timestamp = event.get("timestamp").getAsString();
            String date = LocalDate.parse(timestamp.substring(0,10))
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            Path file = Paths.get("eventstore").resolve(topicName).resolve(topicName + "Feeder").resolve(date + ".events");


            if (topicName.equals("energy")){
                if (Files.exists(file)) {
                    String indicator = String.valueOf(event.get("indicator"));
                    Files.lines(file).forEach(line -> {
                        if(line.contains(indicator)){
                            repeatedLine = true;
                        }
                    });
                }
            } else if (topicName.equals("weather")){
                if (Files.exists(file)) {
                    String city = String.valueOf(event.get("cityName"));
                    Files.lines(file).forEach(line -> {
                        if(line.contains(city)){
                            repeatedLine = true;
                        }
                    });
                }
            }
            if (!repeatedLine) {
                writer.handleEvent(topicName, event);
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
