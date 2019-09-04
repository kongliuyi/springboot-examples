package net.riking.rabbitmq.producer.controller;

import net.riking.rabbitmq.producer.service.FanoutProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {
	@Autowired
	private FanoutProducerService fanoutProducerService;

	@GetMapping("/sendFanout")
	public String sendFanout(String queueName) {
		fanoutProducerService.send(queueName);
		return "success";
	}
}
