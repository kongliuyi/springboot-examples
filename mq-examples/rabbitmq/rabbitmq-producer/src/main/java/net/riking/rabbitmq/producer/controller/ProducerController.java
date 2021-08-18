package net.riking.rabbitmq.producer.controller;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import net.riking.rabbitmq.utils.SerializationUtils;
import net.riking.rabbitmq.vo.MessageVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: 消息通知 - 提供者
 * @Author: kongLiuYi
 * @Date: 2021/5/27 0029 21:24
 */
@RestController
@RequestMapping("/producer")
@Slf4j
public class ProducerController {

    private final AMQP.BasicProperties properties = new AMQP.BasicProperties("text/plain",
            null,
            new HashMap<>(0),
            2,
            0, null, null, null,
            null, null, null, null,
            null, null);



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
     * 验证 Direct、topic
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @param count      发送次数
     * @return success
     */
    @GetMapping("/send/{exchange}/{routingKey}/{count}")
    public String send(@PathVariable String exchange, @PathVariable String routingKey, @PathVariable Integer count) {
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
    public String sendDirectDelay(@PathVariable String exchange, @PathVariable String routingKey, @PathVariable Integer delayTimes) {
        log.info("发送 exchange:{},routingKey:{},delayTimes:{}", exchange, routingKey, delayTimes);
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 执行发送消息到指定队列
        amqpTemplate.convertAndSend(exchange, routingKey, new MessageVo(String.valueOf(UUID.randomUUID()), createTime), message -> {
            message.getMessageProperties().setContentEncoding("utf-8");
            // 两者不能同时设置
            if (exchange.contains("plugin")) {
                // 设置消息延迟时间， 延迟队列,插件形式
                message.getMessageProperties().setDelay(delayTimes);
            } else {
                // 设置消息存活时间 过期时间+死信队列 -> 延迟队列
                message.getMessageProperties().setExpiration(delayTimes.toString());
            }
            return message;
        });
        return "success";
    }


    /**
     * 验证全局消息回调确认, 本质实现来自于 -> @RabbitCallbackService
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @return "success"
     */
    @GetMapping("/sendDirectConfirm/{exchange}/{routingKey}")
    public String sendDirectConfirm(@PathVariable String exchange, @PathVariable String routingKey) {
        log.info("发送 exchange:{},routingKey:{}", exchange, routingKey);
        RabbitTemplate rabbitTemplate = ((RabbitTemplate) amqpTemplate);
        // 构建回调id为uuid
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 执行发送消息到指定队列
        rabbitTemplate.convertAndSend(exchange, routingKey, new MessageVo(String.valueOf(UUID.randomUUID()), createTime), correlationId);
        return "success";
    }


    /**
     * 警告：该方式千万不能用于生产，这种方式本质是在 channel 中田间监听器回调，
     * 由于 channel 不会销毁会重复使用，会造成重复添加回调监听器，并且是全局监听器
     * 验证消息回调确认，需要配置，跟踪源码也未发现如何配置
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @return "success"
     */
    @GetMapping("/sendDirectConfirm2/{exchange}/{routingKey}")
    public String sendDirectConfirm2(@PathVariable String exchange, @PathVariable String routingKey) {
        log.info("发送 exchange:{},routingKey:{}", exchange, routingKey);
        RabbitTemplate rabbitTemplate = ((RabbitTemplate) amqpTemplate);
        rabbitTemplate.invoke(operations -> {
                    String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    //构建回调id为uuid
                    CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
                    // 执行发送消息到指定队列
                    operations.convertAndSend(exchange, routingKey, new MessageVo(String.valueOf(UUID.randomUUID()), createTime), correlationId);
                    return null;
                }, (deliveryTag, multiple) -> log.info("Ack deliveryTag:{},multiple:{}", deliveryTag, multiple)
                , (deliveryTag, multiple) -> log.info("NAck deliveryTag:{},multiple:{}", deliveryTag, multiple));

        return "success";
    }


    /**
     * 验证原生批量 confirm 模式
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @return "success"
     */
    @GetMapping("/sendDirectOriginalConfirm/{exchange}/{routingKey}")
    public String sendDirectOriginalConfirm(@PathVariable String exchange, @PathVariable String routingKey) {
        log.info("发送 exchange:{},routingKey:{}", exchange, routingKey);
        RabbitTemplate rabbitTemplate = ((RabbitTemplate) amqpTemplate);
        rabbitTemplate.execute((channel) -> {
            // 开启消息的确认模式
            channel.confirmSelect();
            for (int i = 0; i < 10; i++) {
                String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                MessageVo messageVo = new MessageVo(String.valueOf(UUID.randomUUID()), createTime);
                channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, SerializationUtils.serialize(messageVo));
            }
            channel.waitForConfirms(1000);
            if (!channel.waitForConfirms()) {
                log.info("send message failed.");
            }
            return channel;
        });
        return "success";
    }


    /**
     * 警告：该方式千万不能用于生产，由于 channel 不会销毁会重复使用，会造成重复添加回调监听器，
     * 并且是全局监听器,如果想强制使用必须每次调用前清理回调监听器，例如
     * channel.clearConfirmListeners();channel.clearReturnListeners();
     * 验证原生异步批量 confirm 模式,
     *
     * @param exchange   交换器
     * @param routingKey 队列交换绑定的路由键
     * @return "success"
     */
    @GetMapping("/sendDirectOriginalAsyncConfirm/{exchange}/{routingKey}")
    public String sendDirectOriginalAsyncConfirm(@PathVariable String exchange, @PathVariable String routingKey) {
        log.info("发送 exchange:{},routingKey:{}", exchange, routingKey);
        RabbitTemplate rabbitTemplate = ((RabbitTemplate) amqpTemplate);
        rabbitTemplate.execute((channel) -> {
            SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<>());
            // 开启消息的确认模式
            channel.confirmSelect();
            channel.clearConfirmListeners();
            channel.clearReturnListeners();
            // 添加一个确认监听
            channel.addConfirmListener(new ConfirmListener() {

                /**
                 * 生产者成功的将消息给交换器时的回调
                 * @param deliveryTag 签收
                 * @param multiple true 多条，false 单条
                 */
                @Override
                public void handleAck(long deliveryTag, boolean multiple) {
                    log.info("Ack, SeqNo: {}, multiple: {}", deliveryTag, multiple);
                    if (multiple) {
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                    }
                }

                /**
                 * 生产者没有成功的将消息给交换器时的回调
                 * @param deliveryTag 签收
                 * @param multiple true 多条，false 单条
                 */
                @Override
                public void handleNack(long deliveryTag, boolean multiple) {
                    log.error("Nack, SeqNo: {}, multiple: {}", deliveryTag, multiple);
                    System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                    if (multiple) {
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                    }
                }
            });

            // 添加一个 return 监听，该监听器的作用：生产者没有成功地将消息路由到队列
            channel.addReturnListener((replyCode, replyText, exchangeReturn, routingKeyReturn, properties, body) -> {
                try {
                    log.info("handleReturn ===> replyCode={} ,replyText={} ,exchangeReturn={} ,routingKeyReturn={},properties={},body={}", replyCode
                            , replyText, exchangeReturn, routingKeyReturn, properties, SerializationUtils.deserialize(body));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            for (int i = 0; i < 10; i++) {
                long nextSeqNo = channel.getNextPublishSeqNo();
                String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                MessageVo messageVo = new MessageVo(String.valueOf(UUID.randomUUID()), createTime);
                // 当 PublisherCallbackChannelImpl 存在，会调用 handleReturn 方法 ， properties.headers 一定不能为 null
                channel.basicPublish(exchange, routingKey, true, properties, SerializationUtils.serialize(messageVo));
                confirmSet.add(nextSeqNo);
            }

            return channel;
        });


        return "success";
    }
}
