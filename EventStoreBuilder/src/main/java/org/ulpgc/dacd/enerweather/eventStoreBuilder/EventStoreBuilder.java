package org.ulpgc.dacd.enerweather.eventStoreBuilder;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.enerweather.eventStoreBuilder.store.JsonMessageListener;

import javax.jms.*;

public class EventStoreBuilder {
    private Session session;

    public void start() {
        try {
            connectToBroker();
            System.out.println("Connected to activemq broker");
        } catch (JMSException e) {
            System.err.println("Failed to connect to activemq broker");
            try {
                Thread.sleep(3000);
                connectToBroker();
                System.out.println("Connected to activemq broker");
            } catch (Exception ex) {
                System.err.println("Failed to connect to activemq broker");
            }
        }
    }

    private void connectToBroker() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("EventStoreClient");
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        subscribe("weather");
        subscribe("energy");

        connection.start();
    }

    private void subscribe(String topicName) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, topicName);
        consumer.setMessageListener(new JsonMessageListener());

    }
}