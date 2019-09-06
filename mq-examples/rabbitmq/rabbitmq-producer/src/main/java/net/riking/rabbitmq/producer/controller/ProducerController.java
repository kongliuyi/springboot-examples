package net.riking.rabbitmq.producer.controller;

import net.riking.rabbitmq.producer.config.DirectDelayConfig;
import net.riking.rabbitmq.producer.config.DirectRefuseConfig;
import net.riking.rabbitmq.producer.config.FanoutConfig;
import net.riking.rabbitmq.producer.config.TopicConfig;
import org.springframework.amqp.core.AmqpTemplate;
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

	/**
	 * 验证SpringBoot与发布订阅整合
	 */
	@GetMapping("/sendFanout")
	public String sendFanout() {
		String msg = "fanout_msg:" + new Date();
		System.out.println( msg);
		amqpTemplate.convertAndSend(FanoutConfig.EXCHANGE_NAME,null, msg);
		return "success";
	}


	/**
	 * 验证SpringBoot与主题模式整合以及重试机制
	 */
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



	/**
	 * 验证SpringBoot与路由模式整合以及（拒绝消息策略）死信队列
	 */
	@GetMapping("/sendDirectRefuse")
	public String sendDirect(@RequestParam String routingKey, @RequestParam long id) {
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String,Object> map=new HashMap<>();
		map.put("messageId",String.valueOf(UUID.randomUUID()));
		map.put("messageData","DirectRefuse_msg:" + new Date());
		map.put("createTime",createTime);
		map.put("id", id);
		amqpTemplate.convertAndSend(DirectRefuseConfig.EXCHANGE_NAME,routingKey, map);
		return "success";
	}


	/**
	 * 验证SpringBoot与路由模式整合以及（延迟或者过期消息策略）死信队列
	 */
	@GetMapping("/sendDirectDelay")
	public String sendDirectDelay(@RequestParam String routingKey, @RequestParam long times) {
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String,Object> map=new HashMap<>();
		map.put("messageId",String.valueOf(UUID.randomUUID()));
		map.put("messageData","DirectDelay:" + new Date());
		map.put("createTime",createTime);
		map.put("times",times);
		amqpTemplate.convertAndSend(DirectDelayConfig.EXCHANGE_NAME,routingKey, map);
		return "success";
	}
}
