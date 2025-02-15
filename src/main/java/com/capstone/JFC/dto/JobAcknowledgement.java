package com.capstone.JFC.dto;

import java.util.UUID;

import com.capstone.JFC.model.AcknowledgementEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobAcknowledgement {

    private String acknowledgementId;
    private AcknowledgementEvent payload;

    @JsonCreator
    public JobAcknowledgement(
            @JsonProperty("acknowledgementId") String acknowledgementId,
            @JsonProperty("payload") AcknowledgementEvent payload) {
        this.acknowledgementId = (acknowledgementId == null || acknowledgementId.isEmpty())
                ? UUID.randomUUID().toString() : acknowledgementId;
        this.payload = payload;
    }

    public JobAcknowledgement() {}

    public String getAcknowledgementId() {
        return acknowledgementId;
    }

    public void setAcknowledgementId(String acknowledgementId) {
        this.acknowledgementId = acknowledgementId;
    }

    public AcknowledgementEvent getPayload() {
        return payload;
    }

    public void setPayload(AcknowledgementEvent payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "JobAcknowledgement{" +
                "acknowledgementId='" + acknowledgementId + '\'' +
                ", payload=" + payload +
                '}';
    }
}