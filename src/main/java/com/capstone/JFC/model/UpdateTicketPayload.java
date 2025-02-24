package com.capstone.JFC.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateTicketPayload {
    
    private String tenantId;
    private String ticketId;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String destinationTopic;

    private String jobId;
    
    public UpdateTicketPayload() {
    }

    public UpdateTicketPayload(String tenantId, String ticketId, String destinationTopic) {
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.destinationTopic = destinationTopic;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getDestinationTopic() {
        return destinationTopic;
    }

    public void setDestinationTopic(String destinationTopic) {
        this.destinationTopic = destinationTopic;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}