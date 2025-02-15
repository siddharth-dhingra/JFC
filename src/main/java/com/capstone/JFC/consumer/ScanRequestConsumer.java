package com.capstone.JFC.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.capstone.JFC.dto.ScanRequestEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.ToolType;
import com.capstone.JFC.service.JobFlowService;

@Component
public class ScanRequestConsumer {

    private final JobFlowService jobFlowService;

    public ScanRequestConsumer(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @KafkaListener(topics = "${app.kafka.topics.jfc-auth}", groupId = "jfc-group",
                   containerFactory = "scanRequestListenerFactory")
    public void onScanRequest(ScanRequestEvent event) {
        // Extract job info
        String jobId = event.getEventId();
        String tenantId = event.getPayload().getTenantId();
        // Convert to jobCategory, e.g. SCAN_PULL_CODESCAN if codescan
        // We'll do a placeholder
        JobCategory cat = mapToolTypeToCategory(event.getPayload().getToolType());

        // Build JSON payload from the event’s details
        String payload = event.getPayload().toString();

        jobFlowService.createNewJob(jobId, cat, tenantId, payload);
    }

    private JobCategory mapToolTypeToCategory(ToolType t) {
        switch (t) {
            case CODESCAN: return JobCategory.SCAN_PULL_CODESCAN;
            case DEPENDABOT: return JobCategory.SCAN_PULL_DEPENDABOT;
            case SECRETSCAN: return JobCategory.SCAN_PULL_SECRETSCAN;
        }
        return JobCategory.SCAN_PULL_CODESCAN;
    }
}