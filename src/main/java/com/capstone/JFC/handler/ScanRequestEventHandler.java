package com.capstone.JFC.handler;

import com.capstone.JFC.dto.ScanRequestEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.ScanEvent;
import com.capstone.JFC.model.ToolType;
import com.capstone.JFC.service.JobFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ScanRequestEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanRequestEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JobFlowService jobFlowService;

    public ScanRequestEventHandler(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @Override
    public void handle(String message) throws Exception {
        LOGGER.info("Handling SCAN_PULL event: {}", message);
        ScanRequestEvent event = objectMapper.readValue(message, ScanRequestEvent.class);

        String jobId = UUID.randomUUID().toString();
        ScanEvent scanEvent = event.getPayload();
        String tenantId = scanEvent.getTenantId();
        String destinationTopic = scanEvent.getDestinationTopic();

        ToolType t = scanEvent.getToolType();
        JobCategory cat = switch (t) {
            case CODESCAN -> JobCategory.SCAN_PULL_CODESCAN;
            case DEPENDABOT -> JobCategory.SCAN_PULL_DEPENDABOT;
            case SECRETSCAN -> JobCategory.SCAN_PULL_SECRETSCAN;
        };

        scanEvent.setJobId(jobId);

        String payloadJson = objectMapper.writeValueAsString(scanEvent);
        jobFlowService.createNewJob(jobId, cat, tenantId, payloadJson, destinationTopic);
        LOGGER.info("Processed ScanRequestEvent: jobId={}, tenantId={}, category={}, destinationTopic={}",
                jobId, tenantId, cat, destinationTopic);
    }
}