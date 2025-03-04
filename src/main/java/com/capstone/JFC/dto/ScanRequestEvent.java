package com.capstone.JFC.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

import com.capstone.JFC.model.Event;
import com.capstone.JFC.model.EventTypes;
import com.capstone.JFC.model.ScanEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanRequestEvent implements Event<ScanEvent> {
    
    private String eventId;
    public static EventTypes TYPE = EventTypes.SCAN_PULL;
    private ScanEvent payload;

    public ScanRequestEvent(ScanEvent payload, String eventId) {
        this.payload = payload;
        this.eventId = (eventId == null || eventId.isEmpty()) ? UUID.randomUUID().toString() : eventId;
    }

    public ScanRequestEvent() {}

    public static EventTypes getTYPE() {
        return TYPE;
    }

    public static void setTYPE(EventTypes tYPE) {
        TYPE = tYPE;
    }

    public void setPayload(ScanEvent payload) {
        this.payload = payload;
    }

    @Override
    public EventTypes getType() {
        return TYPE;
    }

    @Override
    public ScanEvent getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}