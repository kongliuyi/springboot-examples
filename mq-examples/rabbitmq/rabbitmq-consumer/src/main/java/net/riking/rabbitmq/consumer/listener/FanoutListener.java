package net.riking.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.riking.rabbitmq.utils.SerializationUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FanoutListener {

    @RabbitListener(queues = "email.fanOut.queue")
    @RabbitHandler
    public void processEmail(Message message, Channel channel) throws IOException, ClassNotFoundException {
        log.info("邮件消费者获取生产者消息:{}", SerializationUtils.deserialize(message.getBody()));
        long tag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }

    @RabbitListener(queues = "sms.fanOut.queue")
    @RabbitHandler
    public void processSms(Message message, Channel channel) throws IOException, ClassNotFoundException {
        log.info("短信消费者获取生产者消息:{}", SerializationUtils.deserialize(message.getBody()));
        long tag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);
    }
}
