package org.ulpgc.dacd.enerweather.eventStoreBuilder.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JsonMessageListener implements MessageListener {
    private final Gson gson = new Gson();
    private final FileEventWriter writer = new FileEventWriter();

    public void onMessage(Message message) {
        try {
            if (!(message instanceof TextMessage)) return;
            String json = ((TextMessage) message).getText();
            JsonObject event = gson.fromJson(json, JsonObject.class);

            writer.handleEvent(message.getJMSDestination().toString(), event);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
