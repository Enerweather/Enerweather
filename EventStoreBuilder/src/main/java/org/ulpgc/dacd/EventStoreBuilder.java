package org.ulpgc.dacd;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.store.JsonMessageListener;

import javax.jms.*;

public class EventStoreBuilder {
    private Connection connection;
    private Session session;

    public void start() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connection = connectionFactory.createConnection();
        connection.setClientID("EventStoreClient");
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        subscribe("weather.topic");
        subscribe("energy.topic");

        connection.start();
    }

    private void subscribe(String topicName) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, topicName);
        JsonMessageListener listener = new JsonMessageListener();
        consumer.setMessageListener(listener);
    }
}