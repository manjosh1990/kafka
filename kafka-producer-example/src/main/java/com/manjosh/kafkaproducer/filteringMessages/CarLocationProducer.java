package com.manjosh.kafkaproducer.filteringMessages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CarLocationProducer {
    @Autowired
    @Qualifier("kafkaTemplateString")
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    public void send(CarLocation carLocation) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(carLocation);
        kafkaTemplate.send("t-location", json);
    }
}
