package net.riking.rabbitmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }



/*
    @RabbitListener(queues = "fanout_email_queue_retry")
    @RabbitHandler
    public void processRetryByNetwork(String msg) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        // 获取email参数
        String email = jsonObject.getString("email");
        // 请求地址
        String emailUrl = "http://127.0.0.1:8083/sendEmail?email=" + email;
        JSONObject result = HttpClientUtils.httpGet(emailUrl);
        if (result == null) {
            // 因为网络原因,造成无法访问,继续重试
            throw new Exception("调用接口失败!");
        }
        System.out.println("执行结束....");

    }*/




    /**
     * 验证手动签收模式, --需要配置文件
      * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
/*    @RabbitListener(queues = "fanout_email_queue_sign")
    @RabbitHandler
    public void processSign (Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        System.out
                .println(Thread.currentThread().getName() + ",邮件消费者获取生产者消息msg:" + new String(message.getBody(), "UTF-8")
                        + ",messageId:" + message.getMessageProperties().getMessageId());
        // 手动ack
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 手动签收
        channel.basicAck(deliveryTag, false);
    }*/

}
