package net.riking.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.riking.rabbitmq.config.ArgumentsConfig;
import net.riking.rabbitmq.enums.QueueEnum;
import net.riking.rabbitmq.vo.MessageVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 以下3种情况会进入死信队列
 * 1.有消息被拒绝（basic.reject/ basic.nack）并且 requeue=false
 * 2.队列达到最大长度
 * 3.消息TTL过期
 */
@Component
@Slf4j
public class DirectListener {


    @Value(value = "${server.port}")
    private int port;


    /**
     * 1.消息正常消费
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.queue"), exchange = @Exchange(value = "direct.exchange"), key = {"direct.routingKey"})})
    public void process(Map<String, Object> map, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("port:{},direct.queue:{}", port, map.toString());
        // 保证一次只分发一次
        channel.basicQos(1);
        Thread.sleep(1000);
        long tag = message.getMessageProperties().getDeliveryTag();
        // 参数2： 是否批量应答
        channel.basicAck(tag, false);
    }


    /**
     * 1.有消息被拒绝
     * 注释掉 bindings，队列由生产者创建，以下雷同
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "refuse.direct.queue"), exchange = @Exchange(value = "refuse.direct.exchange"), key = {"refuse.direct.routingKey"},
            arguments = {@Argument(name = ArgumentsConfig.X_DEAD_LETTER_EXCHANGE, value = "x.refuse.direct.exchange"), @Argument(name = ArgumentsConfig.X_DEAD_LETTER__ROUTING_KEY, value = "x.refuse.direct.routingKey")})})
    public void processByRefuse(MessageVo messageVo, Message message, Channel channel) throws IOException {
        log.info(QueueEnum.REFUSE_DIRECT_QUEUE + ":{}", messageVo.toString());
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            long result = 1 / System.currentTimeMillis();
            System.out.println("result:" + result);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("direct.refuse.queue:丢弃该消息");
            channel.basicNack(tag, false, false);
        }
    }


    /**
     * 2.有消息被拒绝
     * 死信队列消费者
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "x.refuse.direct.queue"), exchange = @Exchange(value = "x.refuse.direct.exchange"), key = {"x.refuse.direct.routingKey"})})
    public void processDeadByRefuse(MessageVo messageVo, Message message, Channel channel) throws IOException {
        log.info(QueueEnum.X_REFUSE_DIRECT_QUEUE + ":{}", messageVo);
        long tag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }


    /**
     * 3.消息TTL过期
     *
     * @ RabbitListener(queues = ".delay.create")
     */
  /*  @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "delay.direct.queue"), exchange = @Exchange(value = "delay.direct.exchange"), key = {"delay.direct.routingKey"},
            arguments = {@Argument(name = ArgumentsConfig.X_DEAD_LETTER_EXCHANGE, value = "x.delay.direct.exchange"), @Argument(name = ArgumentsConfig.X_DEAD_LETTER__ROUTING_KEY, value = "x.delay.direct.routingKey")})})*/
    @RabbitListener(queues = "delay.direct.queue")
    @RabbitHandler
    public void processByDelay(MessageVo messageVo, Message message, Channel channel) throws Exception {
        log.info(QueueEnum.DELAY_DIRECT_QUEUE.getQueue() + ":{}", messageVo);
        // 保证一次只分发一次
        channel.basicQos(1);
        // 模拟执行任务,时间5秒
        Thread.sleep(5000);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    /**
     * 3.消息TTL过期
     * 死信队列消费者
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "x.delay.direct.queue"), exchange = @Exchange(value = "x.delay.direct.exchange"), key = {"x.delay.direct.routingKey"})})
    public void processDeadByDelay(MessageVo messageVo, Message message, Channel channel) throws IOException {
        log.info(QueueEnum.X_DELAY_DIRECT_QUEUE.getQueue() + ":{}", messageVo);
        long tag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }


}
