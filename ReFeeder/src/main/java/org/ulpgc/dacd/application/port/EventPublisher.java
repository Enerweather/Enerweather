package org.ulpgc.dacd.application.port;

import javax.jms.JMSException;

public interface EventPublisher {
    void publish(String jsonMessage) throws Exception;
    void close() throws JMSException;
}