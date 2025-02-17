package com.capstone.JFC.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateEvent {

    private String tenantId;    
    private ToolType toolType;  
    private long alertNumber;
    private String newState;
    private String reason;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String destinationTopic;

    private String jobId;

    public UpdateEvent() {}

    public UpdateEvent(String tenantId, ToolType toolType, 
                       long alertNumber, String newState, String reason) {
        this.tenantId = tenantId;
        this.toolType = toolType;
        this.alertNumber = alertNumber;
        this.newState = newState;
        this.reason = reason;
    }

    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ToolType getToolType() {
        return toolType;
    }
    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public long getAlertNumber() {
        return alertNumber;
    }
    public void setAlertNumber(long alertNumber) {
        this.alertNumber = alertNumber;
    }

    public String getNewState() {
        return newState;
    }
    public void setNewState(String newState) {
        this.newState = newState;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
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
