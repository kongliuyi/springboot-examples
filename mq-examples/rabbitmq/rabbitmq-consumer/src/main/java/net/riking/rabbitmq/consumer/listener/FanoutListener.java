package net.riking.rabbitmq.consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutListener {

    @RabbitListener(queues = "fanout.mail")
    @RabbitHandler
    public void processEamil(String msg)  {
        System.out.println("邮件消费者获取生产者消息msg:" + msg);
    }

    @RabbitListener(queues = "fanout.sms")
    @RabbitHandler
    public void processSms(String msg) {
        System.out.println("短信消费者获取生产者消息msg:" + msg);
    }
}
