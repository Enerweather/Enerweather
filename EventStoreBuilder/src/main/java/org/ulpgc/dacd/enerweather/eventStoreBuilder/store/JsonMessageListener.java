package org.ulpgc.dacd.enerweather.eventStoreBuilder.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JsonMessageListener implements MessageListener {
    private final FileEventWriter writer = new FileEventWriter();
    @Override
    public void onMessage(Message message) {
        try {
            Gson gson = new Gson();
            if (!(message instanceof TextMessage)) return;
            String json = ((TextMessage) message).getText();
            JsonObject event = gson.fromJson(json, JsonObject.class);

            writer.handleEvent(message.getJMSDestination().toString().substring(8), event);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
