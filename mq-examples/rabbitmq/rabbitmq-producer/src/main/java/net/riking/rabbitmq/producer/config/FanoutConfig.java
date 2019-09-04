package net.riking.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FanoutConfig {

	// 邮件队列
	private final String FANOUT_EMAIL_QUEUE = "fanout_eamil_queue";

	// 短信队列
	private final String FANOUT_SMS_QUEUE = "fanout_sms_queue";

	// 交换机名称-主题交换机
	private final String EXCHANGE_NAME = "fanoutExchange";

	// 1.定义邮件队列
	@Bean
	public Queue fanOutEamilQueue() {
		return new Queue(FANOUT_EMAIL_QUEUE);
	}

	// 2.定义短信队列
	@Bean
	public Queue fanOutSmsQueue() {
		return new Queue(FANOUT_SMS_QUEUE);
	}

	// 3.定义交换机
	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

	// 4.邮件队列与交换机绑定
	@Bean
	Binding bindingExchangeEamil(Queue fanOutEamilQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanOutEamilQueue).to(fanoutExchange);
	}

	// 5.短信队列与交换机绑定
	@Bean
	Binding bindingExchangeSms(Queue fanOutSmsQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanOutSmsQueue).to(fanoutExchange);
	}
}
