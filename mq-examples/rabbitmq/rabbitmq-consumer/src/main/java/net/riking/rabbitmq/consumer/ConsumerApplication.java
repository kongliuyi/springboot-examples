package net.riking.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @RabbitListener(queues = "fanout_eamil_queue")
    @RabbitHandler
    public void processEamil(String msg)  {
        System.out.println("邮件消费者获取生产者消息msg:" + msg);
    }

    @RabbitListener(queues = "fanout_sms_queue")
    @RabbitHandler
    public void processSms(String msg) {
        System.out.println("短信消费者获取生产者消息msg:" + msg);
    }
}
