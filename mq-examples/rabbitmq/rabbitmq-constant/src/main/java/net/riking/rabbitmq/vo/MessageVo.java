package net.riking.rabbitmq.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MessageVo implements Serializable {

    private String id;

    private String createTime;

}
