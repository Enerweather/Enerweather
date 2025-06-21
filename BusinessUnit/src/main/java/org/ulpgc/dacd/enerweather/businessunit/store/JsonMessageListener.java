package org.ulpgc.dacd.enerweather.businessunit.store;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonMessageListener implements MessageListener {
    private final CsvEventWriter writer = new CsvEventWriter();
    @Override
    public void onMessage(Message message) {
        try {
            if (!(message instanceof TextMessage textMessage)) return;
            String json = textMessage.getText();

            JsonObject event = JsonParser.parseString(json)
                    .getAsJsonObject();

            String topicName = message.getJMSDestination().toString().substring(8);

            String timestamp = event.get("timestamp").getAsString();
            String date = LocalDate.parse(timestamp.substring(0,10))
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            Path csvFile = Paths.get("datamart").resolve(topicName).resolve(date + ".csv");
            if (!Files.exists(csvFile)) {
                writer.handleEvent(topicName, event);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
