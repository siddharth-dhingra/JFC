package com.capstone.JFC.dto;

import java.util.UUID;

import com.capstone.JFC.model.AcknowledgementPayload;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobAcknowledgement {

    private String acknowledgementId;
    private AcknowledgementPayload payload;

    @JsonCreator
    public JobAcknowledgement(
            @JsonProperty("acknowledgementId") String acknowledgementId,
            @JsonProperty("payload") AcknowledgementPayload payload) {
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

    public AcknowledgementPayload getPayload() {
        return payload;
    }

    public void setPayload(AcknowledgementPayload payload) {
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