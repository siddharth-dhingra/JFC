package com.capstone.JFC.handler;

import com.capstone.JFC.dto.ScanParseEvent;
import com.capstone.JFC.model.FileLocationEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.ToolType;
import com.capstone.JFC.service.JobFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ScanParseEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanParseEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JobFlowService jobFlowService;

    public ScanParseEventHandler(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @Override
    public void handle(String message) throws Exception {
        LOGGER.info("Handling SCAN_PARSE event: {}", message);
        ScanParseEvent event = objectMapper.readValue(message, ScanParseEvent.class);

        String jobId = UUID.randomUUID().toString();
        FileLocationEvent fileLocationEvent = event.getPayload();
        String tenantId = fileLocationEvent.getTenantId();
        String destinationTopic = fileLocationEvent.getDestinationTopic();

        ToolType toolType = fileLocationEvent.getToolName();
        JobCategory category = switch (toolType) {
            case CODESCAN -> JobCategory.SCAN_PARSE_CODESCAN;
            case DEPENDABOT -> JobCategory.SCAN_PARSE_DEPENDABOT;
            case SECRETSCAN -> JobCategory.SCAN_PARSE_SECRETSCAN;
        };

        fileLocationEvent.setJobId(jobId);

        String payloadJson = objectMapper.writeValueAsString(fileLocationEvent);
        jobFlowService.createNewJob(jobId, category, tenantId, payloadJson, destinationTopic);
        LOGGER.info("Processed ScanParseEvent: jobId={}, tenantId={}, category={}, destinationTopic={}",
                jobId, tenantId, category, destinationTopic);
    }
}