package com.capstone.JFC.service;

import com.capstone.JFC.model.CreateTicketPayload;
import com.capstone.JFC.model.FileLocationEvent;
import com.capstone.JFC.model.ScanEvent;
import com.capstone.JFC.model.UpdateEvent;
import com.capstone.JFC.model.UpdateTicketPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFlowParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobFlowParser.class);
    private final ObjectMapper objectMapper;

    public JobFlowParser() {
        this.objectMapper = new ObjectMapper();
    }

    public ScanEvent parseScanEvent(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(payload, ScanEvent.class);
        } catch (Exception e) {
            LOGGER.error("Error parsing ScanEvent from payload using ObjectMapper: {}", payload, e);
            return null;
        }
    }

    public FileLocationEvent parseFileLocationEvent(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(payload, FileLocationEvent.class);
        } catch (Exception e) {
            LOGGER.error("Error parsing FileLocationEvent from payload using ObjectMapper: {}", payload, e);
            return null;
        }
    }

    public UpdateEvent parseUpdateEvent(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(payload, UpdateEvent.class);
        } catch (Exception e) {
            LOGGER.error("Error parsing UpdateEvent from payload using ObjectMapper: {}", payload, e);
            return null;
        }
    }

    public CreateTicketPayload parseCreateTicketEvent(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(payload, CreateTicketPayload.class);
        } catch (Exception e) {
            LOGGER.error("Error parsing UpdateEvent from payload using ObjectMapper: {}", payload, e);
            return null;
        }
    }

    public UpdateTicketPayload parseUpdateTicketEvent(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(payload, UpdateTicketPayload.class);
        } catch (Exception e) {
            LOGGER.error("Error parsing UpdateEvent from payload using ObjectMapper: {}", payload, e);
            return null;
        }
    }
}