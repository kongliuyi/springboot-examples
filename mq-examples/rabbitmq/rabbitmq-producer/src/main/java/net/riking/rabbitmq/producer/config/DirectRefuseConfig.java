package net.riking.rabbitmq.producer.config;

import net.riking.rabbitmq.config.ArgumentsConfig;
import net.riking.rabbitmq.enums.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用路由模式验证 　消息被拒绝－死信队列
 */
@Component
public class DirectRefuseConfig {


    @Bean
    DirectExchange refuseExchange() {
        return new DirectExchange(QueueEnum.REFUSE_DIRECT_QUEUE.getExchange());
    }

    @Bean
    public Queue refuseQueue() {
        //死信队列配置信息，与DIRECT_QUEUE队列进行绑定
        Map<String, Object> args = new HashMap<>(4);
        //声明死信交换器
        args.put(ArgumentsConfig.X_DEAD_LETTER_EXCHANGE, QueueEnum.X_REFUSE_DIRECT_QUEUE.getExchange());
        //声明死信路由键
        args.put(ArgumentsConfig.X_DEAD_LETTER__ROUTING_KEY, QueueEnum.X_REFUSE_DIRECT_QUEUE.getRoutingKey());
        return new Queue(QueueEnum.REFUSE_DIRECT_QUEUE.getQueue(), true, false, false, args);
    }

    @Bean
    Binding bindingRefuseExchange(Queue refuseQueue, DirectExchange refuseExchange) {
        return BindingBuilder.bind(refuseQueue).to(refuseExchange).with(QueueEnum.REFUSE_DIRECT_QUEUE.getRoutingKey());
    }


    @Bean
    public Queue xRefuseQueue() {
        return new Queue(QueueEnum.X_REFUSE_DIRECT_QUEUE.getQueue(), true);
    }

    @Bean
    public DirectExchange xRefuseExchange() {
        return new DirectExchange(QueueEnum.X_REFUSE_DIRECT_QUEUE.getExchange());
    }

    @Bean
    public Binding bindingXRefuseExchange(Queue xRefuseQueue, DirectExchange xRefuseExchange) {
        return BindingBuilder.bind(xRefuseQueue).to(xRefuseExchange).with(QueueEnum.X_REFUSE_DIRECT_QUEUE.getRoutingKey());
    }


}
