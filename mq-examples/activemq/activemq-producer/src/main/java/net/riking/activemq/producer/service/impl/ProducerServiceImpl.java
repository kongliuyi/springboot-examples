package net.riking.activemq.producer.service.impl;

import javax.jms.Destination;
import javax.jms.Topic;

import net.riking.activemq.producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    private JmsMessagingTemplate jmsMessageTemplate;

    @Autowired
    private Topic topic;

    @Override
    public void sendMsg(Destination destination, String message) {
        jmsMessageTemplate.convertAndSend(destination, message);
    }

    @Override
    public void publish(String msg) {
        jmsMessageTemplate.convertAndSend(topic, msg);
    }

}

