package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.EventPublisher;

import javax.jms.*;

public class MessagePublisher implements EventPublisher, AutoCloseable {
    private static final String BROKERURL = "tcp://localhost:61616";
    private static final String TOPICNAME = "energy";
    private final Connection connection;
    private final Session session;
    private final MessageProducer producer;

    public MessagePublisher() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKERURL);
        connection = connectionFactory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(TOPICNAME);
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    }

    public void publish(String jsonMessage) throws JMSException {
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
