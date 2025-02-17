package com.capstone.JFC.consumer;

import com.capstone.JFC.dto.UpdateAlertEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.UpdateEvent;
import com.capstone.JFC.service.JobFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateAlertRequestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlertRequestConsumer.class);

    private final JobFlowService jobFlowService;

    public UpdateAlertRequestConsumer(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @KafkaListener(topics = "${app.kafka.topics.jfc-auth-update}", groupId = "jfc-update-group", containerFactory = "updateAlertEventListenerContainerFactory")
    public void consumeUpdateAlertRequest(UpdateAlertEvent record) {
        UpdateEvent payload = record.getPayload();
        String eventId = record.getEventId();

        System.out.println("hello"+record.getPayload().toString());
        
        LOGGER.info("Received UpdateAlertEvent => eventId={}, payload={}", eventId, payload);

        // The job category for update finding
        JobCategory category = JobCategory.UPDATE_FINDING;

        // Convert the payload to JSON or store as a string
        String payloadString = payload.toString(); 
        // or new ObjectMapper().writeValueAsString(payload)

        // Insert job in DB with status=NEW
        jobFlowService.createNewJob(eventId, category, payload.getTenantId(), payloadString);

        LOGGER.info("Created new job => jobId={}, category={}, tenantId={}", 
            eventId, category, payload.getTenantId());
    }
}