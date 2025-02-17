package com.capstone.JFC.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanEvent {

    private String tenantId;
    private ToolType toolType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String destinationTopic;

    private String jobId;

    public ScanEvent() {}

    public ScanEvent(String tenantId, ToolType toolType) {
        this.tenantId = tenantId;
        this.toolType = toolType;
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
