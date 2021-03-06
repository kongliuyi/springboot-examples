package net.riking.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 *
 * 通过通配符模式，模拟重试机制
 *     spring.rabbitmq.listener.simple.retry.enabled: true   #开启消费者重试
 *     spring.rabbitmq.listener.simple.retry.max-attempts: 5  #最大重试次数
 *     spring.rabbitmq.listener.simple.retry.initial-interval: 3000    #重试间隔次数
 *
 *     不可处理的异常,应取消重试，改为死信队列或者其他补偿机制
 *
 */
@Component
public class TopicListener {
    /**  在不开启手动签收时
     * 建议：该方式不建议重试，应设置死信队列或者其他补偿机制
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "topic.retry.error")
    @RabbitHandler
    public void processRetryByException(Map<String,Object> map,  Message message, Channel channel) throws IOException {
        System.out.println("topic.retry.error:"+map.toString());
        long tag=message.getMessageProperties().getDeliveryTag();
        try {
            int  i= 1/0;
        }catch (Exception e){
            System.out.println("拒绝消息也相当于主动删除mq队列的消息");
            channel.basicNack(tag, false, false);
        }
    }

    /**
     * 建议：由于网络原因,可以重试
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "topic.retry.info")
    @RabbitHandler
    public void processRetryByNetwork(Map<String,Object> map, Message message, Channel channel) throws Exception {
        System.out.println("topic.retry.network:"+map.toString());
        long tag=message.getMessageProperties().getDeliveryTag();
        // 假设：该判断为HTTP请求结果
       //String httpUrl=   map.get("httpUrl").toString();
       // JSONObject result =     HttpClientUtils.httpGet(httpUrl);
          int result= new Random().nextInt();
          System.out.println("result:"+result);
          if(result%3==0){
              System.out.println("因为网络原因,造成无法访问,继续重试");
              throw new Exception("调用接口失败!");
          }else   if(result%3==1){
              System.out.println("第二种方式继续重试");
              channel.basicNack(tag, false, true);
          }else   if(result%3==2){
              System.out.println("拒绝消息也相当于主动删除mq队列的消息");
              channel.basicNack(tag, false, false);
          }
    }
}
