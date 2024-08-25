package com.manjosh.practice.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {
    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @KafkaListener(topics = "kafka-new-topic",groupId = "manjosh-gp")
    public void consume(String message){
        log.info("consumer1 consumed the message {}",message);
    }

    @KafkaListener(topics = "kafka-new-topic",groupId = "manjosh-gp")
    public void consume2(String message){
        log.info("consumer2 consumed the message {}",message);
    }


    @KafkaListener(topics = "kafka-new-topic",groupId = "manjosh-gp")
    public void consume3(String message){
        log.info("consumer3 consumed the message {}",message);
    }

    @KafkaListener(topics = "kafka-new-topic",groupId = "manjosh-gp")
    public void consume4(String message){
        log.info("consumer4 consumed the message {}",message);
    }
}
