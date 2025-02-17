package com.capstone.JFC.handler;

import com.capstone.JFC.dto.UpdateAlertEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.UpdateEvent;
import com.capstone.JFC.service.JobFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpdateAlertEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlertEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JobFlowService jobFlowService;

    public UpdateAlertEventHandler(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @Override
    public void handle(String message) throws Exception {
        LOGGER.info("Handling UPDATE_FINDING event: {}", message);
        UpdateAlertEvent event = objectMapper.readValue(message, UpdateAlertEvent.class);

        String jobId = UUID.randomUUID().toString();
        UpdateEvent updateEvent = event.getPayload();
        String tenantId = updateEvent.getTenantId();
        String destinationTopic = updateEvent.getDestinationTopic();

        JobCategory category = JobCategory.UPDATE_FINDING;

        updateEvent.setJobId(jobId);

        String payloadJson = objectMapper.writeValueAsString(updateEvent);
        jobFlowService.createNewJob(jobId, category, tenantId, payloadJson, destinationTopic);
        LOGGER.info("Processed UpdateAlertEvent: jobId={}, tenantId={}, destinationTopic={}",
                jobId, tenantId, destinationTopic);
    }
}