package org.ulpgc.dacd.enerweather.weatherFeeder.infrastructure.port;

import javax.jms.JMSException;

public interface EventPublisher extends AutoCloseable {
    void publish(String jsonMessage) throws Exception;
    void close() throws JMSException;
}