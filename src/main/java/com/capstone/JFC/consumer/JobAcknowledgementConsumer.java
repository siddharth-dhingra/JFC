package com.capstone.JFC.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.capstone.JFC.dto.JobAcknowledgement;
import com.capstone.JFC.model.AcknowledgementStatus;
import com.capstone.JFC.service.JobFlowService;
import com.capstone.JFC.service.JobScheduler;

@Component
public class JobAcknowledgementConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobAcknowledgementConsumer.class);

    private final JobFlowService jobFlowService;
    private final JobScheduler jobScheduler;

    public JobAcknowledgementConsumer(JobFlowService jobFlowService, JobScheduler jobScheduler) {
        this.jobFlowService = jobFlowService;
        this.jobScheduler = jobScheduler;
    }

    @KafkaListener(topics = "${app.kafka.topics.job-acknowledgement}", groupId = "jfc-group", containerFactory = "jobAcknowledgementListenerContainerFactory")
    public void consumeJobAcknowledgement(JobAcknowledgement ack) {
        LOGGER.info("Received JobAcknowledgement: {}", ack);
        String jobId = ack.getPayload().getJobId();
        AcknowledgementStatus status = ack.getPayload().getStatus();
        if (status == AcknowledgementStatus.SUCCESS) {
            jobFlowService.markJobAsSuccess(jobId);
        } else {
            jobFlowService.markJobAsFailure(jobId);
        }
        // Trigger scheduling immediately after receiving an acknowledgement to avoid idle time.
        jobScheduler.runSchedulerOnAck();
    }
}