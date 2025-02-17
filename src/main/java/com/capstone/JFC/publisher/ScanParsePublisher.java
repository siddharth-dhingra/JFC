package com.capstone.JFC.publisher;

import com.capstone.JFC.dto.ScanParseEvent;
import com.capstone.JFC.model.FileLocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class ScanParsePublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanParsePublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ScanParsePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String jobId, FileLocationEvent payload, String destination) {
        ScanParseEvent event = new ScanParseEvent(jobId, payload);
        kafkaTemplate.send(destination, event);
        LOGGER.info("Published ScanParseEvent to topic {}: {}", destination, event);
    }
}