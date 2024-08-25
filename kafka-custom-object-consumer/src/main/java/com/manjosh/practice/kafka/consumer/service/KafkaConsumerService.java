package com.manjosh.practice.kafka.consumer.service;

import com.manjosh.practice.kafka.consumer.model.Animal;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "order-event", groupId = "payment-grp",containerFactory = "animalListener")
    public void listen(String animal)
    {
        System.out.println("Received '" + animal +"' from the AnimalTopic." );
    }
}
