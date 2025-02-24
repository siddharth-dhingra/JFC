package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.ScanParseEvent;
import com.capstone.JFC.model.FileLocationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class ScanParsePublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanParsePublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ScanParsePublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String jobId, FileLocationEvent payload, String destination) {
        try {
            ScanParseEvent event = new ScanParseEvent(jobId, payload);
            String jsonMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(destination, jsonMessage);
            LOGGER.info("Published ScanParseEvent to topic {}: {}", destination, event);
        } catch(Exception e){
            LOGGER.error("Error publishing ScanParseEvent: ", e);
        }
    }
}