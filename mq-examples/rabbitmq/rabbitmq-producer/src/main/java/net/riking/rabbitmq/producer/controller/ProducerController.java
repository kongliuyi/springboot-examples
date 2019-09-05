package net.riking.rabbitmq.producer.controller;

import net.riking.rabbitmq.producer.config.DirectConfig;
import net.riking.rabbitmq.producer.config.FanoutConfig;
import net.riking.rabbitmq.producer.config.TopicConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/producer")
public class ProducerController {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@GetMapping("/sendFanout")
	public String sendFanout() {
		String msg = "fanout_msg:" + new Date();
		System.out.println( msg);
		amqpTemplate.convertAndSend(FanoutConfig.EXCHANGE_NAME,null, msg);
		return "success";
	}

	@GetMapping("/sendDirect")
	public String sendDirect(@RequestParam String routingKey, @RequestParam long id) {
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String,Object> map=new HashMap<>();
		map.put("messageId",String.valueOf(UUID.randomUUID()));
		map.put("messageData","Direct_msg:" + new Date());
		map.put("createTime",createTime);
		map.put("id", id);
		amqpTemplate.convertAndSend(DirectConfig.EXCHANGE_NAME,routingKey, map);
		return "success";
	}

	@GetMapping("/sendTopic")
	public String sendTopic(@RequestParam String routingKey) {
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "Topic_msg:" + new Date();
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String,Object> map=new HashMap<>();
		map.put("messageId",messageId);
		map.put("messageData",messageData);
		map.put("createTime",createTime);
		map.put("httpUrl", "https://github.com/kongliuyi");
		amqpTemplate.convertAndSend(TopicConfig.EXCHANGE_NAME, routingKey, map);
		return "success";
	}
}
