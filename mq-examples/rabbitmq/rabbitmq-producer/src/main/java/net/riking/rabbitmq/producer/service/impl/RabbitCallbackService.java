package net.riking.rabbitmq.producer.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.riking.rabbitmq.producer.service.IRabbitCallbackService;
import net.riking.rabbitmq.utils.SerializationUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Description: rabbitTemplate 单例的,回调方面只能全局处理
 * 用来确认生产者 producer 将消息发送到 broker ，broker 上的交换机 exchange 再投递给队列 queue的过程中，消息是否成功投递。
 * 1.消息从 producer 到 rabbitmq broker 有一个 confirmCallback 确认模式。
 * 2.消息从 exchange 到 queue 投递失败有一个 returnCallback 退回模式。
 * @Author: kongLiuYi
 * @Date: 2021/5/27 0029 21:24
 */
@Slf4j
@Service
public class RabbitCallbackService implements IRabbitCallbackService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitCallbackService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 设置消息回调确认方法，是否成功投递到交换器中
     *
     * @param correlationData 请求数据对象
     * @param ack             是否发送成功
     * @param cause           发送失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("消息发送异常!");
        } else {
            log.info("已经收到确认，correlationData={} ,ack={}, cause={}", correlationData, true, cause);
        }
    }

    /**
     * 消息投递到队列失败回调处理
     *
     * @param message    消息体
     * @param replyCode  响应代码
     * @param replyText  响应内容
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    @SneakyThrows
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 补偿机制
        log.info("投递到队列失败，message = {} replyCode = {} ,replyText = {} ,exchange = {} ,routingKey = {}", SerializationUtils.deserialize(message.getBody()), replyCode, replyText, exchange, routingKey);
    }

    @PostConstruct
    public void setCallback() {
        // 确保消息发送失败后可以重新返回到队列中 ,
        // 注意：yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public Object doInRabbit(RabbitOperations operations) {

        return null;
    }
}
