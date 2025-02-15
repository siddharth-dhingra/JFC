package com.capstone.JFC.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    private String jobId; // from the event's eventId or jobId

    @Enumerated(EnumType.STRING)
    private JobCategory jobCategory;

    private String tenantId;

    @Lob
    private String payload; // JSON or a string representing the details from the event

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDateTime timestampCreated;
    private LocalDateTime timestampUpdated;

    public Job() {}

    // constructor, getters, setters, etc.
    public Job(String jobId, JobCategory jobCategory, String tenantId, String payload,
               JobStatus status, LocalDateTime timestampCreated, LocalDateTime timestampUpdated) {
        this.jobId = jobId;
        this.jobCategory = jobCategory;
        this.tenantId = tenantId;
        this.payload = payload;
        this.status = status;
        this.timestampCreated = timestampCreated;
        this.timestampUpdated = timestampUpdated;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobCategory getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(JobCategory jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public LocalDateTime getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(LocalDateTime timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    } 
}
