package net.riking.rabbitmq.producer.service.impl;

import net.riking.rabbitmq.producer.service.FanoutProducerService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FanoutProducerServiceImpl implements FanoutProducerService {
	@Autowired
	private AmqpTemplate amqpTemplate;

	public void send(String queueName) {
		String msg = "fanout_msg:" + new Date();
		System.out.println(queueName + ":" + msg);
		amqpTemplate.convertAndSend(queueName, msg);
	}
}
