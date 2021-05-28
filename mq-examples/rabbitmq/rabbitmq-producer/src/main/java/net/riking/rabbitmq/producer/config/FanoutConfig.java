package net.riking.rabbitmq.producer.config;

import net.riking.rabbitmq.enums.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FanoutConfig {


    /**
     * 1.定义邮件队列
     */
    @Bean
    public Queue fanOutEmailQueue() {
        return new Queue(QueueEnum.FANOUT_EMAIL_QUEUE.getQueue());
    }


    /**
     * 2.定义短信队列
     */
    @Bean
    public Queue fanOutSmsQueue() {
        return new Queue(QueueEnum.FANOUT_SMS_QUEUE.getQueue());
    }

    /**
     * 3.定义交换机
     */
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(QueueEnum.FANOUT_EMAIL_QUEUE.getExchange());
    }

    /**
     * 4.邮件队列与交换机绑定
     */
    @Bean
    Binding bindingExchangeEmail(Queue fanOutEmailQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutEmailQueue).to(fanoutExchange);
    }

    /**
     * 5.短信队列与交换机绑定
     */
    @Bean
    Binding bindingExchangeSms(Queue fanOutSmsQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutSmsQueue).to(fanoutExchange);
    }
}
