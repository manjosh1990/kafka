package com.manjosh.kafkaproducer.controller;

import com.manjosh.kafkaproducer.dto.Customer;
import com.manjosh.kafkaproducer.service.KafkaMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer-app")
public class EventController {

    @Autowired
    private KafkaMessagePublisher publisher;

    @GetMapping("/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable String message) {
        try{
            for (int i = 1; i < 100; i++)
                publisher.sendMessageToTopic(message+ " : "+i);
            return ResponseEntity.ok("message published");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/publish")
    public void sendEvent(@RequestBody Customer customer){
        publisher.sendEventsToTopic(customer);
    }
}
