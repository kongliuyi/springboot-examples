package net.riking.activemq.producer.controller;

import javax.jms.Destination;

import net.riking.activemq.producer.service.ProducerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {
    @Autowired
    private ProducerService producerService;

    @GetMapping("/p2p")
    public String p2p(@RequestParam String msg) {
        Destination destination = new ActiveMQQueue("my.queue");
        producerService.sendMsg(destination, msg);
        return "P2pProducer success："+msg;
    }

    @GetMapping("/topic")
    public Object topic(String msg) {
        producerService.publish(msg);
        return "topicProducer success："+msg;
    }
}