package net.riking.rabbitmq.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * 使用通配符模式验证 重试机制
 */
@Component
public class TopicConfig {

    // 交换机名称-主题交换机
    public  final static String EXCHANGE_NAME = "topicExchange";

    // 队列名称
    private  final static String  TOPIC_RETRY_ERROR_QUEUE   = "topic.retry.error";

    // 队列名称
    private  final static String  TOPIC_RETRY_INFO_QUEUE   = "topic.retry.info";


    // 1.交换机
    @Bean
    TopicExchange topicExchange(){
        return  new TopicExchange(EXCHANGE_NAME);
    }

    // 2.队列
    @Bean
    public Queue topicQueueRetryError() {
        return new Queue(TOPIC_RETRY_ERROR_QUEUE);
    }

    // 2.队列
    @Bean
    public Queue topicQueueRetryInfo() {
        return new Queue(TOPIC_RETRY_INFO_QUEUE);
    }


    // 3.队列与交换机绑定
    @Bean
    Binding bindingExchangeRetryErrot(Queue topicQueueRetryError, TopicExchange topicExchange) {
        return  BindingBuilder.bind(topicQueueRetryError).to(topicExchange).with("topic.routingKey.retry.*");
    }

    // 3.队列与交换机绑定
    @Bean
    Binding bindingExchangeRetryInfo(Queue topicQueueRetryInfo, TopicExchange topicExchange) {
        return  BindingBuilder.bind(topicQueueRetryInfo).to(topicExchange).with("topic.routingKey.retry.#");
    }

}
