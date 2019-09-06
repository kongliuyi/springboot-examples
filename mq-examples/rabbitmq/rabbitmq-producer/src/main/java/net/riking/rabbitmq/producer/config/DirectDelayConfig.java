package net.riking.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用路由模式验证 　消息TTL过期－死信队列
 */
@Component
public class DirectDelayConfig {

    /**
     * 定义正常队列相关信息
     */
    public  final static String  EXCHANGE_NAME = "direct.delay.exchange";
    private final static String  DIRECT_QUEUE = "direct.delay.queue";
    private  final static String DIRECT_ROUTING_KEY = "direct.delay.routingKey";


    /**
     * 定义死信队列相关信息
     */
    private final static String X_DELAY_EXCHANGE_NAME = "x.direct.delay.exchange";
    private final static String X_DELAY_QUEUE = "x.direct.delay.queue";
    private final static String X_DELAY_ROUTING_KEY = "x.direct.delay.routingKey";



    // 1.交换机
    @Bean
    DirectExchange directDelayExchange(){
        return  new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue directDelayQueue() {
        //死信队列配置信息，与DIRECT_QUEUE队列进行绑定
        Map<String, Object> args = new HashMap<>(3);
        //声明死信交换器
        args.put(ArgumentsConfig.X_DEAD_LETTER_EXCHANGE, X_DELAY_EXCHANGE_NAME);
        //声明死信路由键
        args.put(ArgumentsConfig.X_DEAD_LETTER__ROUTING_KEY, X_DELAY_ROUTING_KEY);
        //声明队列消息过期时间
        args.put(ArgumentsConfig.X_MESSAGE_TTL, 10000);
        return new Queue(DIRECT_QUEUE, true, false, false, args);
    }

    // 3.队列与交换机绑定
    @Bean
    Binding bindingExchangeDirectDelay(Queue directDelayQueue, DirectExchange directDelayExchange) {
        return  BindingBuilder.bind(directDelayQueue).to(directDelayExchange).with(DIRECT_ROUTING_KEY);
    }


    //1.配置死信队列
    @Bean
    public Queue delayQueue() {
        return new Queue(X_DELAY_QUEUE, true);
    }

    //2.配置死信交换机
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(X_DELAY_EXCHANGE_NAME);
    }

    //3.配置死信交换机与死信队列绑定
    @Bean
    public Binding bindingDelayExchange(Queue delayQueue, DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(X_DELAY_ROUTING_KEY);
    }


}
