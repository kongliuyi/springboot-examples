package net.riking.rabbitmq.producer.config;

import net.riking.rabbitmq.config.ArgumentsConfig;
import net.riking.rabbitmq.enums.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @Description: 延迟队列, 插件形式，
 * 通过安装插件，自定义交换机，让交换机拥有延迟发送消息的能力，从而实现延迟消息。
 * 延迟队列在我们的项目中可以应用于很多场景，如：下单后两个消息取消订单，七天自动收货，七天自动好评，密码冻结后24小时解冻等
 * @Author: kongLiuYi
 * @Date: 2021/5/27 0029 21:24
 */
@Component
public class DirectDelayPluginConfig {


    /**
     * 1.配置死信交换机
     *
     * @return DirectExchange
     */
    @Bean
    public CustomExchange xDelayPluginExchange() {

        // 死信交换机，使用插件 rabbitmq_delayed_message_exchange
        Map<String, Object> arguments = new HashMap<>(2);
        arguments.put(ArgumentsConfig.X_DELAYED_TYPE, "direct");
        return new CustomExchange(QueueEnum.X_DELAY_PLUGIN_DIRECT_QUEUE.getExchange(), "x-delayed-message", true, false, arguments);
    }


    /**
     * 2.配置死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue xDelayPluginQueue() {
        return new Queue(QueueEnum.X_DELAY_PLUGIN_DIRECT_QUEUE.getQueue(), true);
    }


    /**
     * 3.配置死信交换机与死信队列绑定、路由key
     *
     * @param xDelayPluginQueue    死信队列
     * @param xDelayPluginExchange 死信交换机
     * @return Binding
     */
    @Bean
    public Binding xDelayPluginBinding(Queue xDelayPluginQueue, Exchange xDelayPluginExchange) {
        return BindingBuilder.bind(xDelayPluginQueue).to(xDelayPluginExchange).with(QueueEnum.X_DELAY_PLUGIN_DIRECT_QUEUE.getRoutingKey()).noargs();
    }


}
