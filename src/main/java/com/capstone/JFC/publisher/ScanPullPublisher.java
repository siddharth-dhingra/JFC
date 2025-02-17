package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.ScanRequestEvent;
import com.capstone.JFC.model.ScanEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class ScanPullPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanPullPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ScanPullPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String jobId, ScanEvent payload, String destination) {
        ScanRequestEvent event = new ScanRequestEvent(payload, jobId);
        kafkaTemplate.send(destination, event);
        LOGGER.info("Published ScanRequestEvent to topic {}: {}", destination, event);
    }
}