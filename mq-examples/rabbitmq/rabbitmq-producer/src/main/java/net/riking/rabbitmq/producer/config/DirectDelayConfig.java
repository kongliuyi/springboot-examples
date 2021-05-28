package net.riking.rabbitmq.producer.config;

import net.riking.rabbitmq.config.ArgumentsConfig;
import net.riking.rabbitmq.enums.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @Description:
 * 使用路由模式验证 　消息TTL过期－> 死信队列 = 延迟队列
 * 当队列中消息设置 TTL 并不进行消费，则可以组成延迟队列
 * 延迟队列在我们的项目中可以应用于很多场景，如：下单后两个消息取消订单，七天自动收货，七天自动好评，密码冻结后24小时解冻等
 * @Author: kongLiuYi
 * @Date: 2021/5/27 0029 21:24
 */
@Component
public class DirectDelayConfig {


    /**
     * 1.交换机
     *
     * @return DirectExchange
     */
    @Bean
    DirectExchange delayExchange() {
        return new DirectExchange(QueueEnum.DELAY_DIRECT_QUEUE.getExchange());
    }

    /**
     * 2.队列
     *
     * @return Queue
     */
    @Bean
    public Queue delayQueue() {
        // 死信队列配置信息，与 DIRECT_QUEUE 队列进行绑定
        return QueueBuilder
                .durable(QueueEnum.DELAY_DIRECT_QUEUE.getQueue())
                // 配置到期后转发的交换器
                .withArgument(ArgumentsConfig.X_DEAD_LETTER_EXCHANGE, QueueEnum.X_DELAY_DIRECT_QUEUE.getExchange())
                // 配置到期后转发的路由键
                .withArgument(ArgumentsConfig.X_DEAD_LETTER__ROUTING_KEY, QueueEnum.X_DELAY_DIRECT_QUEUE.getRouteKey())
                //.withArgument(ArgumentsConfig.X_MESSAGE_TTL, 10000)
                .build();
    }


    /**
     * 3.队列与交换机绑定
     *
     * @param delayQueue /
     * @param delayExchange /
     * @return Binding
     */
    @Bean
    Binding delayBinding(Queue delayQueue, DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(QueueEnum.DELAY_DIRECT_QUEUE.getRouteKey());
    }


    /**
     * 1.配置死信交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange xDelayExchange() {
        return new DirectExchange(QueueEnum.X_DELAY_DIRECT_QUEUE.getExchange());
    }


    /**
     * 2.配置死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue xDelayQueue() {
        return new Queue(QueueEnum.X_DELAY_DIRECT_QUEUE.getQueue(), true);
    }


    /**
     * 3.配置死信交换机与死信队列绑定
     *
     * @param xDelayQueue    死信队列
     * @param xDelayExchange 死信交换机
     * @return Binding
     */
    @Bean
    public Binding xDelayBinding(Queue xDelayQueue, DirectExchange xDelayExchange) {
        return BindingBuilder.bind(xDelayQueue).to(xDelayExchange).with(QueueEnum.X_DELAY_DIRECT_QUEUE.getRouteKey());
    }


}
