package com.capstone.JFC.handler;

import com.capstone.JFC.dto.RunbookEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.RunbookPayload;
import com.capstone.JFC.service.JobFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RunbookEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunbookEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JobFlowService jobFlowService;

    public RunbookEventHandler(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @Override
    public void handle(String message) throws Exception {
        LOGGER.info("Handling RUNBOOK event: {}", message);
        try {
            RunbookEvent event = objectMapper.readValue(message, RunbookEvent.class);
            String jobId = UUID.randomUUID().toString();
            RunbookPayload payload = event.getPayload();
            String tenantId = payload.getTenantId();
            String destinationTopic = payload.getDestinationTopic();
            JobCategory category = JobCategory.RUNBOOK;
            payload.setJobId(jobId);
            String payloadJson = objectMapper.writeValueAsString(payload);
            jobFlowService.createNewJob(jobId, category, tenantId, payloadJson, destinationTopic);
            LOGGER.info("Processed Runbook Event: jobId={}, tenantId={}, category={}, destinationTopic={}",
                    jobId, tenantId, category, destinationTopic);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to parse RunbookEvent message: {}", message, e);
        }
    }
}