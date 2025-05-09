package org.ulpgc.dacd;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessagePublisher {
    private static String BROKER_URL = "tcp://localhost:61616";
    private static String TOPIC_NAME = "weather.topic"; // Queue Name.You can create any/many queue names as per your requirement.

    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public MessagePublisher() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(TOPIC_NAME);
        producer = session.createProducer(destination);
    }

    public void sendMessage(String jsonMessage) throws JMSException {
        TextMessage message = session.createTextMessage(jsonMessage);
        producer.send(message);
        System.out.println("Message sent" + jsonMessage);
    }
    public void close() throws JMSException {
        producer.close();
        session.close();
        connection.close();
    }
}
