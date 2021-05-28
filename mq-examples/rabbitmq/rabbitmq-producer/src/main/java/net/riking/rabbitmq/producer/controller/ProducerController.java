package net.riking.rabbitmq.producer.controller;

import lombok.extern.slf4j.Slf4j;
import net.riking.rabbitmq.vo.MessageVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @Description: 消息通知 - 提供者
 * @Author: kongLiuYi
 * @Date: 2021/5/27 0029 21:24
 */
@RestController
@RequestMapping("/producer")
@Slf4j
public class ProducerController {

    private final AmqpTemplate amqpTemplate;

    public ProducerController(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    /**
     * 发布订阅
     *
     * @param exchange 交换器
     * @param count    发送次数
     * @return "success"
     */
    @GetMapping("/sendFanout/{exchange}/{count}")
    public String sendFanout(@PathVariable String exchange, @PathVariable Integer count) {
        log.info("发送 direct: exchange:{},count:{}", exchange, count);
        String uuid = String.valueOf(UUID.randomUUID());
        for (int i = 0; i < count; i++) {
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            amqpTemplate.convertAndSend(exchange, null, new MessageVo(uuid + i, createTime));
        }
        return "success";
    }


    /**
     * 验证 Direct
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @param count      发送次数
     * @return success
     */
    @GetMapping("/sendDirect/{exchange}/{routingKey}/{count}")
    public String sendDirect(@PathVariable String exchange, @PathVariable String routingKey, @PathVariable Integer count) {
        log.info("发送 direct: exchange:{},routingKey:{}，count:{}", exchange, routingKey, count);
        String uuid = String.valueOf(UUID.randomUUID());
        for (int i = 0; i < count; i++) {
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            amqpTemplate.convertAndSend(exchange, routingKey, new MessageVo(uuid + i, createTime));
        }
        return "success";
    }


    /**
     * 发送延迟消息
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @param delayTimes 延迟时长，单位：毫秒
     * @return "success"
     */
    @GetMapping("/sendDirectDelay/{exchange}/{routingKey}/{delayTimes}")
    public String sendDirectDelay(@PathVariable String exchange, @PathVariable String routingKey, @PathVariable String delayTimes) {
        log.info("发送 exchange:{},routingKey:{},delayTimes:{}", exchange, routingKey, delayTimes);
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 执行发送消息到指定队列
        amqpTemplate.convertAndSend(exchange, routingKey, new MessageVo(String.valueOf(UUID.randomUUID()), createTime), message -> {
            message.getMessageProperties().setContentEncoding("utf-8");
            // 设置消息存活时间
            message.getMessageProperties().setExpiration(delayTimes);
            return message;
        });
        return "success";
    }

}
