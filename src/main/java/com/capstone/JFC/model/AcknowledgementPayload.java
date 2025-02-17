package com.capstone.JFC.model;


public class AcknowledgementPayload {
    
    private AcknowledgementStatus status = AcknowledgementStatus.SUCCESS;
    private String jobId;

    public AcknowledgementPayload() {}

    public AcknowledgementPayload(String jobId) {
        this.jobId = jobId;
    }

    public AcknowledgementStatus getStatus() {
        return status;
    }

    public void setStatus(AcknowledgementStatus status) {
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "AcknowledgementEvent{" +
                "status=" + status +
                ", jobId='" + jobId + '\'' +
                '}';
    }
}