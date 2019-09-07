package net.riking.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
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
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.refuse.queue"), exchange = @Exchange(value = "direct.refuse.exchange"), key = {"direct.refuse.routingKey"})})
    @RabbitHandler
    public void processByRefuse(Map<String,Object> map, Message message, Channel channel) throws IOException {
        System.out.println("direct.refuse.queue:"+map.toString());
        long tag=message.getMessageProperties().getDeliveryTag();
        try {
            long result=1/(long) map.get("id");
            System.out.println("result:" + result);
            channel.basicAck(tag, false);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("丢弃该消息" );
            channel.basicNack(tag, false, false);
        }
    }


    /**1.有消息被拒绝
     * 死信队列消费者
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "x.direct.refuse.queue"), exchange = @Exchange(value = "x.direct.refuse.exchange"), key = {"x.direct.refuse.routingKey"})})
    @RabbitHandler
    public void processDeadByRefuse(Map<String,Object> map, Message message, Channel channel) throws IOException {
        System.out.println("x.direct.refuse.queue:"+map.toString());
        long tag=message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }



    /**
     * 3.消息TTL过期
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.delay.queue"), exchange = @Exchange(value = "direct.delay.exchange"), key = {"direct.delay.routingKey"})})
    @RabbitHandler
    public void processByDelay(Map<String,Object> map, Message message, Channel channel) throws Exception {
        System.out.println("direct.delay.queue:"+map.toString());
        channel.basicQos(1);// 保证一次只分发一次
        long times= (long) map.get("times");
        // 模拟执行任务,时间5秒
          Thread.sleep(times);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    /**3.消息TTL过期
     * 死信队列消费者
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "x.direct.delay.queue"), exchange = @Exchange(value = "x.direct.delay.exchange"), key = {"x.direct.delay.routingKey"})})
    @RabbitHandler
    public void processDeadByDelay(Map<String,Object> map, Message message, Channel channel) throws IOException {
        System.out.println("x.direct.delay.queue:"+map.toString());
        long tag=message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }



}
