package com.capstone.JFC.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileLocationEvent {

    private String tenantId;
    private String filePath;
    private ToolType toolName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String destinationTopic;

    private String jobId;

    public FileLocationEvent() {}

    public FileLocationEvent(String tenantId, String filePath, ToolType toolName) {
        this.tenantId = tenantId;
        this.filePath = filePath;
        this.toolName = toolName;
    }

    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ToolType getToolName() {
        return toolName;
    }
    public void setToolName(ToolType toolName) {
        this.toolName = toolName;
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
