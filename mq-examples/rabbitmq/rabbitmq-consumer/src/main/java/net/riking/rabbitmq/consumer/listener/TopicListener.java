package net.riking.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.riking.rabbitmq.vo.MessageVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

/**
 * 通过通配符模式，模拟重试机制
 * spring.rabbitmq.listener.simple.retry.enabled: true   #开启消费者重试
 * spring.rabbitmq.listener.simple.retry.max-attempts: 5  #最大重试次数
 * spring.rabbitmq.listener.simple.retry.initial-interval: 3000    #重试间隔次数
 * <p>
 * 不可处理的异常,应取消重试，改为死信队列或者其他补偿机制
 */
@Component
@Slf4j
public class TopicListener {


    /**
     * Topic 消费消息，在不开启手动签收时
     *
     * @param messageVo 消息
     * @param message   消息
     * @param channel   /
     * @throws IOException /
     */
    @RabbitListener(queues = "topic.message.queue")
    @RabbitHandler
    public void process(MessageVo messageVo, Message message, Channel channel) throws IOException {
        log.info("topic.message.queue:{}", messageVo);
        long tag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }


    /**
     * Topic 消费消息，在不开启手动签收时
     * 建议：该方式不建议重试，应设置死信队列或者其他补偿机制
     *
     * @param messageVo 消息
     * @param message   消息
     * @param channel   /
     * @throws IOException /
     */
    @RabbitListener(queues = "topic.retry.error")
    @RabbitHandler
    public void processRetryByException(MessageVo messageVo, Message message, Channel channel) throws IOException {
        log.info("topic.retry.error:{}", messageVo);
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            System.out.println("拒绝消息也相当于主动删除mq队列的消息");
            channel.basicNack(tag, false, false);
        }
    }

    /**
     * Topic 消费消息，在不开启手动签收时
     * 建议：由于网络原因,可以重试
     *
     * @param messageVo 消息
     * @param message   消息
     * @param channel   /
     * @throws IOException /
     */
    @RabbitListener(queues = "topic.retry.info")
    @RabbitHandler
    public void processRetryByNetwork(MessageVo messageVo, Message message, Channel channel) throws Exception {
        log.info("topic.retry.network:{}", messageVo);
        long tag = message.getMessageProperties().getDeliveryTag();
        // 假设：该判断为HTTP请求结果
        int result = new Random().nextInt();
        log.info("result:" + result);
        if (result % 3 == 0) {
            log.info("因为网络原因,造成无法访问,继续重试");
            throw new Exception("调用接口失败!");
        } else if (result % 3 == 1) {
            log.info("第二种方式继续重试");
            channel.basicNack(tag, false, true);
        } else if (result % 3 == 2) {
            log.info("拒绝消息也相当于主动删除mq队列的消息");
            channel.basicNack(tag, false, false);
        }
    }
}
