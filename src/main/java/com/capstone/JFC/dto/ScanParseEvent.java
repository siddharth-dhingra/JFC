package com.capstone.JFC.dto;

import java.util.UUID;

import com.capstone.JFC.model.Event;
import com.capstone.JFC.model.EventTypes;
import com.capstone.JFC.model.FileLocationEvent;

public class ScanParseEvent implements Event<FileLocationEvent> {
    
    private String eventId;
    public static EventTypes TYPE = EventTypes.SCAN_PARSE;
    private FileLocationEvent payload;

    public ScanParseEvent() {}

    public ScanParseEvent(String eventId, FileLocationEvent payload) {
        this.eventId = (eventId == null || eventId.isEmpty()) ? UUID.randomUUID().toString() : eventId;
        this.payload = payload;
    }

    public static EventTypes getTYPE() {
        return TYPE;
    }

    public static void setTYPE(EventTypes tYPE) {
        TYPE = tYPE;
    }

    public void setPayload(FileLocationEvent payload) {
        this.payload = payload;
    }

    @Override
    public EventTypes getType() {
        return TYPE;
    }

    @Override
    public FileLocationEvent getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }
}