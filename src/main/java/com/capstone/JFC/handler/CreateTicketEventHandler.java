package com.capstone.JFC.handler;

import com.capstone.JFC.dto.CreateTicketEvent;
import com.capstone.JFC.model.CreateTicketPayload;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.service.JobFlowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateTicketEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanParseEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JobFlowService jobFlowService;

    public CreateTicketEventHandler(JobFlowService jobFlowService) {
        this.jobFlowService = jobFlowService;
    }

    @Override
    public void handle(String message) throws Exception {
        LOGGER.info("Handling TICKET_CREATE event: {}", message);
        System.out.println("hello");
        try {
            CreateTicketEvent event = objectMapper.readValue(message, CreateTicketEvent.class);
            System.out.println("hello1");
            String jobId = UUID.randomUUID().toString();
            CreateTicketPayload createTicketEvent = event.getPayload();
            String tenantId = createTicketEvent.getTenantId();
            String destinationTopic = createTicketEvent.getDestinationTopic();

            JobCategory category = JobCategory.TICKETING_CREATE;

            createTicketEvent.setJobId(jobId);
            System.out.println("hello2");
            String payloadJson = objectMapper.writeValueAsString(createTicketEvent);
            jobFlowService.createNewJob(jobId, category, tenantId, payloadJson, destinationTopic);
            System.out.println("hello3");
            LOGGER.info("Processed Ticket Create Event: jobId={}, tenantId={}, category={}, destinationTopic={}",
                    jobId, tenantId, category, destinationTopic);
        } catch (JsonProcessingException e) {
            // Log the error and handle the exception
            LOGGER.error("Failed to parse CreateTicketEvent message: {}", message, e);
        }
    }
}