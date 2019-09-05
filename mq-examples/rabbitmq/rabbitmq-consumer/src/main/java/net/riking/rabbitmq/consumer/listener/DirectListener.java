package net.riking.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 以下3种情况会进入死信队列
 * 1.有消息被拒绝（basic.reject/ basic.nack）并且requeue=false
 * 2.队列达到最大长度
 * 3.消息TTL过期
 *
 */
@Component
public class DirectListener {

    /**
     * 1.有消息被拒绝
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "direct.error")
    @RabbitHandler
    public void processRetryByException(Map<String,Object> map, Message message, Channel channel) throws IOException {
        System.out.println("direct.error:"+message.toString());
        long tag=message.getMessageProperties().getDeliveryTag();
        try {
            long result=1/(long) map.get("id");;
            System.out.println("result:" + result);
            channel.basicAck(tag, false);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("丢弃该消息" );
            channel.basicNack(tag, false, false);
        }
    }


    /**
     * 死信队列消费者
     * @param message
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.dead"), exchange = @Exchange(value = "deadDirectExchange"), key = {"direct.routingKey.dead"})})
    @RabbitHandler
    public void processDeadByException(Map<String,Object> message)  {
        System.out.println("direct.dead:"+message.toString());
    }
}
