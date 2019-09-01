package net.riking.activemq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @JmsListener(destination = "my.queue")
    public void receive(String text) {
        System.out.println("P2pConsumer" + text);
    }



    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }

    @JmsListener(destination = "my.topic" , containerFactory = "jmsListenerContainerTopic")
    public void receiver1(String text ) {
        System.out.println("TopicConsumer : receiver1 : " + text);
    }

    @JmsListener(destination = "my.topic" , containerFactory = "jmsListenerContainerTopic")
    public void receiver2(String text) {
        System.out.println("TopicConsumer : receiver2 : " + text);
    }

    @JmsListener(destination = "my.topic" , containerFactory = "jmsListenerContainerTopic")
    public void receiver3(String text) {
        System.out.println("TopicConsumer : receiver3 : " + text);
    }
}
