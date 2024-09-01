package com.manjosh.practice.kafka.consumer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.kafkaproducer.filteringMessages.CarLocation;
import com.manjosh.practice.kafka.consumer.model.Animal;
import com.manjosh.practice.kafka.consumer.model.Customer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapAddress;

    @Value("${group.id}")
    private String groupId;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public ConsumerFactory<String, String> consumerFactory()
    {
        // Creating a map of string-object type
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "120000");
        // Returning message in JSON format
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public ConsumerFactory<String, Customer> consumerFactory2()
    {
        // Creating a map of string-object type
        Map<String, Object> config = new HashMap<>();
        JsonDeserializer<Customer> deserializer = new JsonDeserializer<>(Customer.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("com.manjosh.practice.kafka.consumer.model");
        deserializer.setUseTypeMapperForKey(true);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "customConsumer-gp");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        // Returning message in JSON format
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> animalListener()
    {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Customer> customerEventListener()
    {
        ConcurrentKafkaListenerContainerFactory<String, Customer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory2());
        return factory;
    }


    //kafka consumer config for filtering messages
    @Bean
    public ConsumerFactory<Object, Object> consumerFactoryObject() {
        var properties = kafkaProperties.buildConsumerProperties(null);
        properties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "120000");
        return new DefaultKafkaConsumerFactory<>(properties);
    }
    @Bean(name = "farLocationContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> farLocationContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactoryObject());
        factory.setRecordFilterStrategy(consumerRecord -> {
            try {
                CarLocation carLocation = objectMapper.readValue(consumerRecord.value().toString(), CarLocation.class);
                return carLocation.getDistance() <= 100;
            } catch (JsonProcessingException e) {
                return false;
            }
        });
        return factory;
    }

}
