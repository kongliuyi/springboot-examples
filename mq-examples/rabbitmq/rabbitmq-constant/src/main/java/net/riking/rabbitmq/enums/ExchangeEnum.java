package net.riking.rabbitmq.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum ExchangeEnum {
    ;


    /**
     * 交换器名称
     */
    private String exchange;

    /**
     * 队列
     */
    private List<QueueEnum> queueEnumList;

    ExchangeEnum(String exchange, List<QueueEnum> queueEnumList) {
        this.exchange = exchange;
        this.queueEnumList = queueEnumList;
    }
}
