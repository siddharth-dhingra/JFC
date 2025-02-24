package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.ScanRequestEvent;
import com.capstone.JFC.model.ScanEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class ScanPullPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanPullPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ScanPullPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String jobId, ScanEvent payload, String destination) {

        try {
            ScanRequestEvent event = new ScanRequestEvent(payload, jobId);
            String jsonMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(destination, jsonMessage);
            LOGGER.info("Published ScanRequestEvent to topic {}: {}", destination, event);
        } catch(Exception e){
            LOGGER.error("Error publishing ScanRequestEvent: ", e);
        }
    }
}