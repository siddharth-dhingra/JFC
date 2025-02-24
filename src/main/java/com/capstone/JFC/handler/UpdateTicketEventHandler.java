package com.capstone.JFC.handler;

import com.capstone.JFC.dto.UpdateTicketEvent;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.UpdateTicketPayload;
import com.capstone.JFC.service.JobFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpdateTicketEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanParseEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JobFlowService jobFlowService;

    public UpdateTicketEventHandler(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @Override
    public void handle(String message) throws Exception {
        LOGGER.info("Handling TICKET_UPDATE event: {}", message);
        UpdateTicketEvent event = objectMapper.readValue(message, UpdateTicketEvent.class);

        String jobId = UUID.randomUUID().toString();
        UpdateTicketPayload updateTicketEvent = event.getPayload();
        String tenantId = updateTicketEvent.getTenantId();
        String destinationTopic = updateTicketEvent.getDestinationTopic();

        JobCategory category = JobCategory.TICKETING_UPDATE;

        updateTicketEvent.setJobId(jobId);

        String payloadJson = objectMapper.writeValueAsString(updateTicketEvent);
        jobFlowService.createNewJob(jobId, category, tenantId, payloadJson, destinationTopic);
        LOGGER.info("Processed ScanParseEvent: jobId={}, tenantId={}, category={}, destinationTopic={}",
                jobId, tenantId, category, destinationTopic);
    }
}