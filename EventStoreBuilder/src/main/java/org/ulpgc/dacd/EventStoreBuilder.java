package org.ulpgc.dacd;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.store.JsonMessageListener;

import javax.jms.*;

public class EventStoreBuilder {
    private Connection connection;
    private Session session;

    public void start()  {
        try {
            connectToBroker();
            System.out.println("Connected to activemq broker");
        } catch (JMSException e) {
            System.err.println("Failed to connect to activemq broker");
            try {
                    Thread.sleep(3000);
                    connectToBroker();
                    System.out.println("Connected to activemq broker");
                } catch (Exception ex){
                    System.err.println("Failed to connect to activemq broker");
                }
            }
        }

        private void connectToBroker() throws JMSException{
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