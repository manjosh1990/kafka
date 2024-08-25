package com.manjosh.kafkaproducer.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, Object> template;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future = template.send("kafka-new-topic", message);
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
}
