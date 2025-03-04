package com.capstone.JFC.consumer;

import com.capstone.JFC.handler.CreateTicketEventHandler;
import com.capstone.JFC.handler.RunbookEventHandler;
import com.capstone.JFC.handler.ScanParseEventHandler;
import com.capstone.JFC.handler.ScanRequestEventHandler;
import com.capstone.JFC.handler.UpdateAlertEventHandler;
import com.capstone.JFC.handler.UpdateTicketEventHandler;
import com.capstone.JFC.model.EventTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UnifiedConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScanRequestEventHandler scanRequestEventHandler;
    private final ScanParseEventHandler scanParseEventHandler;
    private final UpdateAlertEventHandler updateAlertEventHandler;
    private final CreateTicketEventHandler createTicketEventHandler;
    private final UpdateTicketEventHandler updateTicketEventHandler;
    private final RunbookEventHandler runbookEventHandler;

    public UnifiedConsumer(ScanRequestEventHandler scanRequestEventHandler,
                           ScanParseEventHandler scanParseEventHandler,
                           UpdateAlertEventHandler updateAlertEventHandler, 
                           CreateTicketEventHandler createTicketEventHandler,
                           UpdateTicketEventHandler updateTicketEventHandler, RunbookEventHandler runbookEventHandler) {
        this.scanRequestEventHandler = scanRequestEventHandler;
        this.scanParseEventHandler = scanParseEventHandler;
        this.updateAlertEventHandler = updateAlertEventHandler;
        this.createTicketEventHandler = createTicketEventHandler;
        this.updateTicketEventHandler = updateTicketEventHandler;
        this.runbookEventHandler = runbookEventHandler;
    }

    @KafkaListener(
        topics = "${app.kafka.topics.jfc-unified}", 
        groupId = "jfc-group-unified",
        containerFactory = "unifiedListenerContainerFactory"
    )
    public void onMessage(String message) throws Exception {

        JsonNode root = objectMapper.readTree(message);
        String typeString = root.get("type").asText(); 
        EventTypes eventType = EventTypes.valueOf(typeString);

        switch (eventType) {
            case SCAN_PULL:
                scanRequestEventHandler.handle(message);
                break;
            case SCAN_PARSE:
                scanParseEventHandler.handle(message);
                break;
            case UPDATE_FINDING:
                updateAlertEventHandler.handle(message);
                break;
            case TICKETING_CREATE:
                createTicketEventHandler.handle(message);
                break;
            case TICKETING_UPDATE:
                updateTicketEventHandler.handle(message);
                break;
            case RUNBOOK:
                runbookEventHandler.handle(message);
                break;
            default:
                System.err.println("Unknown event type: " + eventType);
        }
    }
}