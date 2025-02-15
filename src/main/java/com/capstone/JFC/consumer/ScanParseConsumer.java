package com.capstone.JFC.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.capstone.JFC.dto.ScanParseEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.ToolType;
import com.capstone.JFC.service.JobFlowService;

@Component
public class ScanParseConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanParseConsumer.class);

    private final JobFlowService jobFlowService;

    public ScanParseConsumer(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @KafkaListener(
        topics = "${app.kafka.topics.tool-jfc}",
        groupId = "jfc-group-scan-parse",
        containerFactory = "fileLocationEventListenerFactory"
    )
    public void onScanParseEvent(ScanParseEvent event) {
        // Extract eventId and payload
        String jobId = event.getEventId();
        String tenantId = event.getPayload().getTenantId();
        ToolType toolType = event.getPayload().getToolName();

        // Map the tool type to a corresponding scan-parse job category
        JobCategory category = mapToolTypeToScanParseCategory(toolType);

        // Convert the event payload to a String for storage (you might use JSON serialization here)
        String payload = event.getPayload().toString();

        // Create a new job with status NEW in the JFC's MySQL DB
        jobFlowService.createNewJob(jobId, category, tenantId, payload);
        LOGGER.info("Created new job for ScanParseEvent with jobId={}, category={}, tenantId={}",
                jobId, category, tenantId);
    }

    private JobCategory mapToolTypeToScanParseCategory(ToolType toolType) {
        switch (toolType) {
            case CODESCAN:
                return JobCategory.SCAN_PARSE_CODESCAN;
            case DEPENDABOT:
                return JobCategory.SCAN_PARSE_DEPENDABOT;
            case SECRETSCAN:
                return JobCategory.SCAN_PARSE_SECRETSCAN;
            default:
                return JobCategory.SCAN_PARSE_CODESCAN;
        }
    }
}
