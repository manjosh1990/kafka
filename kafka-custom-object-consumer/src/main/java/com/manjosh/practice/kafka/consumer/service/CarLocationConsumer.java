package com.manjosh.practice.kafka.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.kafkaproducer.filteringMessages.CarLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CarLocationConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-location", groupId = "cg-all-location")
    public void listenAll(String message) throws JsonMappingException, JsonProcessingException {
        var carLocation = objectMapper.readValue(message, CarLocation.class);
        log.info("listenAll : {}", carLocation);
    }

    @KafkaListener(topics = "t-location", groupId = "cg-far-location",containerFactory = "farLocationContainerFactory")
    public void listenFar(String message) throws JsonMappingException, JsonProcessingException {
        var carLocation = objectMapper.readValue(message, CarLocation.class);
        log.info("---listenFar: {}", carLocation);
    }
}
