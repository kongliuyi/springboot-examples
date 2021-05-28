package net.riking.rabbitmq.enums;

import lombok.Getter;

@Getter
public enum QueueEnum {

    /**
     * 邮件队列
     */
    FANOUT_EMAIL_QUEUE("fanOut.exchange", "email.fanOut.queue", null),
    /**
     * 短信队列
     */
    FANOUT_SMS_QUEUE("fanOut.exchange", "sms.fanOut.queue", null),
    /**
     * 消息通知队列
     */
    DIRECT_QUEUE("direct.exchange", "direct.queue", "direct.routeKey"),

    /**
     * 消息 ttl 延迟队列
     */
    DELAY_DIRECT_QUEUE("delay.direct.exchange", "delay.direct.queue", "delay.direct.routeKey"),
    /**
     * 消息 ttl 延迟队列
     */
    X_DELAY_DIRECT_QUEUE("x.delay.direct.exchange", "x.delay.direct.queue", "x.delay.direct.routeKey"),

    /**
     * 消息 ttl 延迟队列
     */
    REFUSE_DIRECT_QUEUE("refuse.direct.exchange", "refuse.direct.queue", "refuse.direct.routeKey"),

    /**
     * 消息 ttl 延迟队列
     */
    X_REFUSE_DIRECT_QUEUE("x.refuse.direct.exchange", "x.refuse.direct.queue", "x.refuse.direct.routeKey"),

    /**
     * 消息 ttl 延迟队列
     */
    TOPIC_ERROR_QUEUE("topic.exchange", "error.topic.queue", "error.*.topic.routeKey"),
    /**
     * 消息 ttl 延迟队列
     */
    TOPIC_INFO_QUEUE("topic.exchange", "info.topic.queue", "info.#.topic.routeKey");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String queue;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String queue, String routeKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routeKey = routeKey;
    }
}
