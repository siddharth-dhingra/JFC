package com.capstone.JFC.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RunbookPayload {
    private String tenantId;
    private String jobId;
    private List<String> findingIds;
    private TriggerType triggerType; 
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String destinationTopic;

    public RunbookPayload() {}

    public RunbookPayload(String tenantId, String jobId, List<String> findingIds, TriggerType triggerType, String destinationTopic) {
        this.tenantId = tenantId;
        this.jobId = jobId;
        this.findingIds = findingIds;
        this.triggerType = triggerType;
        this.destinationTopic = destinationTopic;
    }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public List<String> getFindingIds() { return findingIds; }
    public void setFindingIds(List<String> findingIds) { this.findingIds = findingIds; }

    public TriggerType getTriggerType() { return triggerType; }
    public void setTriggerType(TriggerType triggerType) { this.triggerType = triggerType; }

    public String getDestinationTopic() { return destinationTopic; }
    public void setDestinationTopic(String destinationTopic) { this.destinationTopic = destinationTopic; }
}