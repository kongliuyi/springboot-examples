package net.riking.rabbitmq.config;

/**
 *   队列标识符
 */
public interface ArgumentsConfig {

    /**
     * 死信交换器标识符
     */
    String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    /**
     * 死信路由键标识符
     */
    String X_DEAD_LETTER__ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 消息过期时间-标识符
     */
     String X_MESSAGE_TTL = "x-message-ttl";

    /**
     * 消息空闲存活时间-标识符
     */
     String X_EXPRTES="x-expires";
    /**
     * 队列最大长度-标识符
     */
     String  X_MAX_LENGTH="x-max-length";
    /**
     * 队列最大占用空间-标识符
     */
      String X_MAX_LENGTH_BYTES="x-max-length-bytes";
    /**
     * 队列优先级别-标识符
     */
    String ALTERNATE_EXCHANGE="alternate-exchange";
    /**
     * AE-标识符
     */
    String X_MAX_PRIORITY="x-max-priority";


    /**
     * 死信交换机类型
     */
    String X_DELAYED_TYPE="x-delayed-type";






}
