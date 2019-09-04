package net.riking.rabbitmq.producer.service;

public interface FanoutProducerService {
    void send(String queueName);
}
