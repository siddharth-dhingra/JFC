package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.UpdateAlertEvent;
import com.capstone.JFC.model.UpdateEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class UpdateAlertPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlertPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UpdateAlertPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String jobId, UpdateEvent payload, String destination) {
        try {
            UpdateAlertEvent event = new UpdateAlertEvent(jobId, payload);
            String jsonMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(destination, jsonMessage);
            LOGGER.info("Published UpdateAlertEvent to topic {}: {}", destination, jsonMessage);
        } catch(Exception e){
            LOGGER.error("Error publishing UpdateAlertEvent: ", e);
        }
    }
}