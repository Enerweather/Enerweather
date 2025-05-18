package org.ulpgc.dacd.enerweather.businessunit.store;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JsonMessageListener implements MessageListener {
    private final CsvEventWriter writer = new CsvEventWriter();
    @Override
    public void onMessage(Message message) {
        try {
            if (!(message instanceof TextMessage textMessage)) return;
            String json = textMessage.getText();

            JsonObject event = JsonParser.parseString(json)
                    .getAsJsonObject();

            String fullDest = message.getJMSDestination().toString();
            String topicName = fullDest.substring(fullDest.indexOf("://") + 3);

            writer.handleEvent(topicName, event);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
