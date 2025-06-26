package org.ulpgc.dacd.enerweather.businessunit.store;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    private final CsvEventWriter writer = new CsvEventWriter();
    private boolean repeatedLine = false;
    @Override
    public void onMessage(Message message) {
        try {
            repeatedLine = false;
            if (!(message instanceof TextMessage textMessage)) return;
            String json = textMessage.getText();

            JsonObject event = JsonParser.parseString(json)
                    .getAsJsonObject();

            String topicName = message.getJMSDestination().toString().substring(8);

            String timestamp = event.get("timestamp").getAsString();
            String date = LocalDate.parse(timestamp.substring(0,10))
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            Path csvFile = Paths.get("datamart").resolve(topicName).resolve(topicName + "Feeder").resolve(date + ".csv");
            if (topicName.equals("energy")){
                if (Files.exists(csvFile)) {
                    String indicator = event.get("indicator").getAsString();
                    Files.lines(csvFile).forEach(line -> {
                        if(line.contains(indicator)){
                            repeatedLine = true;
                        }
                    });
                }
            } else if (topicName.equals("weather")){
                if (Files.exists(csvFile)) {
                    String city = event.get("cityName").getAsString();
                    Files.lines(csvFile).forEach(line -> {
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
