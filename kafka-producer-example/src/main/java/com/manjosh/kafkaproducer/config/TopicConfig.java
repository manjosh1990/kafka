package com.manjosh.kafkaproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Bean
    public NewTopic newTopic(){
        return new NewTopic("kafka-new-topic",3,(short) 1);
    }
}
