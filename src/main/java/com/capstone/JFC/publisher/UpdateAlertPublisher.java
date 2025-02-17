package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.UpdateAlertEvent;
import com.capstone.JFC.model.UpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class UpdateAlertPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlertPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UpdateAlertPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String jobId, UpdateEvent payload, String destination) {
        UpdateAlertEvent event = new UpdateAlertEvent(jobId, payload);
        kafkaTemplate.send(destination, event);
        LOGGER.info("Published UpdateAlertEvent to topic {}: {}", destination, event);
    }
}