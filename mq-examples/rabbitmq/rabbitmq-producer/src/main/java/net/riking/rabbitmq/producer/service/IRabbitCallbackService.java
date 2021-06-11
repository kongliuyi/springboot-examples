package net.riking.rabbitmq.producer.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public interface IRabbitCallbackService extends RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, RabbitTemplate.OperationsCallback{
}
