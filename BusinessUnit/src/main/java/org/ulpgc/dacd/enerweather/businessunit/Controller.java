package org.ulpgc.dacd.enerweather.businessunit;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Controller {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String CLIENT_ID = "BusinessUnitClient";

    private Connection connection;
    private Session session;

    /**
     * Starts the JMS connection, sets up subscribers, and begins message flow.
     */
    public void start() {
        try {
            connectToBroker();
            System.out.println("EventStoreBuilder connected and listening on topics.");
        } catch (JMSException e) {
            System.err.println("Initial connection failed: " + e.getMessage());
            retryOnce();
        }
    }

    /**
     * Attempt a single reconnect after a short pause.
     */
    private void retryOnce() {
        try {
            Thread.sleep(3000);
            connectToBroker();
            System.out.println("Reconnected successfully.");
        } catch (Exception ex) {
            System.err.println("Retry failed: " + ex.getMessage());
            // Depending on your needs, you might exit or keep retrying here.
        }
    }

    private void connectToBroker() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.setClientID(CLIENT_ID);
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

    /**
     * Gracefully shut down JMS resources.
     */
    public void stop() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println("Error closing JMS resources: " + e.getMessage());
        }
    }
}