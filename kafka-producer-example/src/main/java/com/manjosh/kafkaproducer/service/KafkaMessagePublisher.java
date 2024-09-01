package com.manjosh.kafkaproducer.service;

import com.manjosh.kafkaproducer.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessagePublisher.class);
    private final KafkaTemplate<String, Object> template;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future = template.send("kafka-new-topic",2,null, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("sent message =[ " + message + " ] " +
                        "with offset =[ " + result.getRecordMetadata().offset() + " ] " +
                        "and partition =[ " + result.getProducerRecord().partition() + " ]");
            } else {
                System.out.println("unable to send message=[ " + message + " ] due to " + ex.getMessage());
            }
        });
    }

    public void sendEventsToTopic(Customer customer) {
        try {
            CompletableFuture<SendResult<String, Object>> future = template.send("kafka-event", customer);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("sent message =[ " + customer + " ] " +
                            "with offset =[ " + result.getRecordMetadata().offset() + " ] " +
                            "and partition =[ " + result.getProducerRecord().partition() + " ]");
                } else {
                    System.out.println("unable to send message=[ " + customer + " ] due to " + ex.getMessage());
                }
            });
        }catch (Exception e){
            log.error("error occurred while publishing message :  ",e);
        }
    }
}
