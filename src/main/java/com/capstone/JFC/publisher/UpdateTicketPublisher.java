package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.UpdateTicketEvent;
import com.capstone.JFC.model.UpdateTicketPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class UpdateTicketPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlertPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; 

    public UpdateTicketPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String jobId, UpdateTicketPayload payload, String destination) {
        try {
            UpdateTicketEvent event = new UpdateTicketEvent(jobId, payload);
            String jsonMessage = objectMapper.writeValueAsString(event); 
            kafkaTemplate.send(destination, jsonMessage);
            LOGGER.info("Published UpdateTicketEvent to topic {}: {}", destination, jsonMessage);
        } catch (Exception e) {
            LOGGER.error("Error publishing UpdateTicketEvent: ", e); 
        }
    }
}