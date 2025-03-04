package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.RunbookEvent;
import com.capstone.JFC.model.RunbookPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RunbookPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunbookPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public RunbookPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String jobId, RunbookPayload payload, String destination) {
        try {
            RunbookEvent event = new RunbookEvent(payload);
            String jsonMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(destination, jsonMessage);
            LOGGER.info("Published Runbook Event to topic {}: {}", destination, jsonMessage);
        } catch (Exception e) {
            LOGGER.error("Error publishing Runbook Event: ", e);
        }
    }
}