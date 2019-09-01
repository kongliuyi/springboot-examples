package net.riking.activemq.producer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

@Component
public class ActiveMqConfig {
	@Autowired
	private Environment env;

	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(env.getProperty("spring.activemq.broker-url"));
		connectionFactory.setUserName(env.getProperty("spring.activemq.user"));
		connectionFactory.setPassword(env.getProperty("spring.activemq.password"));
		return connectionFactory;
	}

	@Bean
	public JmsTemplate genJmsTemplate() {
		return new JmsTemplate(connectionFactory());

	}

	@Bean
	public JmsMessagingTemplate jmsMessageTemplate() {
		return new JmsMessagingTemplate(connectionFactory());
	}
/**  注：以上注入Bean，可以不写，因为springboot已经封装好了                   */

	//注入topic
	@Bean
	public Topic topic() {
		return new ActiveMQTopic("my.topic");
	}
}
