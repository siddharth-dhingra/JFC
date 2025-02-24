package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.CreateTicketEvent;
import com.capstone.JFC.model.CreateTicketPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class CreateTicketPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlertPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate; 
    private final ObjectMapper objectMapper; 

    public CreateTicketPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String jobId, CreateTicketPayload payload, String destination) {
        try {
            CreateTicketEvent event = new CreateTicketEvent(payload, jobId);
            String jsonMessage = objectMapper.writeValueAsString(event); 
            kafkaTemplate.send(destination, jsonMessage);
            LOGGER.info("Published Create Ticket Event to topic {}: {}", destination, jsonMessage); 
        } catch (Exception e) {
            LOGGER.error("Error publishing CreateTicketEvent: ", e); 
        }
    }
}