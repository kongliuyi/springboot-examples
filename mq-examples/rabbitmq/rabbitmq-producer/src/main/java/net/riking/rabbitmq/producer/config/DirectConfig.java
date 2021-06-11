package net.riking.rabbitmq.producer.config;

import net.riking.rabbitmq.enums.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description: direct 类型验证
 * @Author: kongLiuYi
 * @Date: 2021/5/27 0029 21:24
 */
@Component
public class DirectConfig {


    /**
     * 1.配置交换机
     *
     * @return DirectExchange
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(QueueEnum.DIRECT_QUEUE.getExchange());
    }

    /**
     * 2.配置队列
     *
     * @return Queue
     */
    @Bean
    public Queue directQueue() {
        return new Queue(QueueEnum.DIRECT_QUEUE.getQueue());
    }

    /**
     * 3.队列与交换机绑定
     *
     * @return Queue
     */
    @Bean
    Binding bindingExchangeDirect(Queue directQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue).to(directExchange).with(QueueEnum.DIRECT_QUEUE.getRoutingKey());
    }

}
