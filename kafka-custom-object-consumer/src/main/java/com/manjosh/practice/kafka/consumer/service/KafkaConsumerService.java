package com.manjosh.practice.kafka.consumer.service;

import com.manjosh.practice.kafka.consumer.model.Animal;
import com.manjosh.practice.kafka.consumer.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "order-event", groupId = "payment-grp",containerFactory = "animalListener")
    public void listen(String animal)
    {
        System.out.println("Received '" + animal +"' from the AnimalTopic." );
    }

    @KafkaListener(topics = "kafka-event",groupId = "customConsumer-gp",containerFactory = "customerEventListener")
    public void consumeCustomer(Customer customer){
        log.debug("consumed custom object {}",customer.toString());
    }

    @KafkaListener(topics = "kafka-new-topic",groupId = "customConsumer-gp",topicPartitions ={@TopicPartition(topic = "kafka-new-topic",partitions = {"2"})} )
    public void consumeCustomerString(String customer){
        log.debug("consumed customer string {}",customer);
    }
}
