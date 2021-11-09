package net.riking.rabbitmq.producer.config;

import net.riking.rabbitmq.enums.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * 使用通配符模式验证 重试机制
 */
@Component
public class TopicConfig {


    // 1.交换机
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(QueueEnum.TOPIC_ERROR_QUEUE.getExchange());
    }

    // 2.队列
    @Bean
    public Queue topicQueueRetryError() {
        return new Queue(QueueEnum.TOPIC_ERROR_QUEUE.getQueue());
    }

    // 2.队列
    @Bean
    public Queue topicQueueRetryInfo() {
        return new Queue(QueueEnum.TOPIC_INFO_QUEUE.getQueue());
    }


    // 2.队列
    @Bean
    public Queue topicQueue() {
        return new Queue(QueueEnum.TOPIC_QUEUE.getQueue());
    }


    // 3.队列与交换机绑定
    @Bean
    Binding bindingTopicExchangeRetryError(Queue topicQueueRetryError, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueRetryError).to(topicExchange).with(QueueEnum.TOPIC_ERROR_QUEUE.getRoutingKey());
    }

    // 3.队列与交换机绑定
    @Bean
    Binding bindingTopicExchangeRetryInfo(Queue topicQueueRetryInfo, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueRetryInfo).to(topicExchange).with(QueueEnum.TOPIC_INFO_QUEUE.getRoutingKey());
    }

    // 3.队列与交换机绑定
    @Bean
    Binding bindingTopicExchange(Queue topicQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with(QueueEnum.TOPIC_QUEUE.getRoutingKey());
    }

}
