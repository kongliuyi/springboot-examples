package net.riking.activemq.producer.service;

import javax.jms.Destination;

public interface ProducerService {
    /**
     * 使用指定消息队列发送
     * 
     * @param destination
     * @param message
     */
    void sendMsg(Destination destination, final String message);

    /**
     * 消息发布者
     *
     * @param msg
     */
    void publish(String msg);
}


