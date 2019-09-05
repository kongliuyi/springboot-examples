package net.riking.rabbitmq.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用路由模式验证 死信队列
 */
@Component
public class DirectConfig {

    /**
     * 定义正常队列相关信息
     */
    public  final static String EXCHANGE_NAME = "direct.exchange";
    private final static String DIRECT_QUEUE = "direct.error";
    private  final static String DIRECT_ROUTING_KEY = "direct.routingKey";


    /**
     * 定义死信队列相关信息
     */
    private final static String REFUSE_EXCHANGE_NAME = "direct.refuse.exchange";
    private final static String REFUSE_QUEUE = "direct.refuse.queue";
    private final static String REFUSE_ROUTING_KEY = "direct.refuse.routingKey";


    /**
     * 死信交换器标识符
     */
    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    /**
     * 死信路由键标识符
     */
    public static final String X_DEAD_LETTER__ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 消息过期时间-标识符
     */
    public static final String X_MESSAGE_TTL = "x-message-ttl";



    // 1.交换机
    @Bean
    DirectExchange directExchange(){
        return  new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue directQueue() {
        //死信队列配置信息，与DIRECT_QUEUE队列进行绑定
        Map<String, Object> args = new HashMap<>(2);
        //声明死信交换器
        args.put(X_DEAD_LETTER_EXCHANGE, REFUSE_EXCHANGE_NAME);
        //声明死信路由键
        args.put(X_DEAD_LETTER__ROUTING_KEY, REFUSE_ROUTING_KEY);
        //声明队列消息过期时间
        args.put(X_MESSAGE_TTL, 10000);
        return new Queue(DIRECT_QUEUE, true, false, false, args);
    }

    // 3.队列与交换机绑定
    @Bean
    Binding bindingExchangeDirect(Queue directQueue, DirectExchange directExchange) {
        return  BindingBuilder.bind(directQueue).to(directExchange).with(DIRECT_ROUTING_KEY);
    }


    //1.配置死信队列
    @Bean
    public Queue refuseQueue() {
        return new Queue(REFUSE_QUEUE, true);
    }

    //2.配置死信交换机
    @Bean
    public DirectExchange refuseExchange() {
        return new DirectExchange(REFUSE_EXCHANGE_NAME);
    }

    //3.配置死信交换机与死信队列绑定
    @Bean
    public Binding bindingRefuseExchange(Queue refuseQueue, DirectExchange refuseExchange) {
        return BindingBuilder.bind(refuseQueue).to(refuseExchange).with(REFUSE_ROUTING_KEY);
    }


}
