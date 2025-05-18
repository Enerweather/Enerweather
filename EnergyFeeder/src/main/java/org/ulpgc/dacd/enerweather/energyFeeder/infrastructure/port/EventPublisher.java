package org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.port;

import javax.jms.JMSException;

public interface EventPublisher {
    void publish(String jsonMessage) throws Exception;
    void close() throws JMSException;
}