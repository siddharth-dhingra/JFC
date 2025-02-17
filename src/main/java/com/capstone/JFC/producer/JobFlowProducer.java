package com.capstone.JFC.producer;

import com.capstone.JFC.model.FileLocationEvent;
import com.capstone.JFC.model.Job;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.ScanEvent;
import com.capstone.JFC.model.UpdateEvent;
import com.capstone.JFC.publisher.ScanParsePublisher;
import com.capstone.JFC.publisher.ScanPullPublisher;
import com.capstone.JFC.publisher.UpdateAlertPublisher;
import com.capstone.JFC.service.JobFlowParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobFlowProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobFlowProducer.class);

    @SuppressWarnings("unused")
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final JobFlowParser parser;
    private final ScanPullPublisher scanPullPublisher;
    private final ScanParsePublisher scanParsePublisher;
    private final UpdateAlertPublisher updateAlertPublisher;

    public JobFlowProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.parser = new JobFlowParser();
        this.scanPullPublisher = new ScanPullPublisher(kafkaTemplate);
        this.scanParsePublisher = new ScanParsePublisher(kafkaTemplate);
        this.updateAlertPublisher = new UpdateAlertPublisher(kafkaTemplate);
    }

    public void publishScheduledJobs(JobCategory category, List<Job> jobs) {
        for (Job j : jobs) {
            switch (category) {
                case SCAN_PULL_CODESCAN:
                case SCAN_PULL_DEPENDABOT:
                case SCAN_PULL_SECRETSCAN:
                    ScanEvent scanEvent = parser.parseScanEvent(j.getPayload());
                    if (scanEvent != null) {
                        scanEvent.setJobId(j.getJobId());
                        scanPullPublisher.publish(j.getJobId(), scanEvent, j.getDestinationTopic());
                    } else {
                        LOGGER.error("Failed to parse ScanEvent from payload: {}", j.getPayload());
                    }
                    break;
                case SCAN_PARSE_CODESCAN:
                case SCAN_PARSE_DEPENDABOT:
                case SCAN_PARSE_SECRETSCAN:
                    FileLocationEvent fle = parser.parseFileLocationEvent(j.getPayload());
                    if (fle != null) {
                        fle.setJobId(j.getJobId());
                        scanParsePublisher.publish(j.getJobId(), fle, j.getDestinationTopic());
                    } else {
                        LOGGER.error("Failed to parse FileLocationEvent from payload: {}", j.getPayload());
                    }
                    break;
                case UPDATE_FINDING:
                    UpdateEvent updateEvent = parser.parseUpdateEvent(j.getPayload());
                    if (updateEvent != null) {
                        updateEvent.setJobId(j.getJobId());
                        updateAlertPublisher.publish(j.getJobId(), updateEvent, j.getDestinationTopic());
                    } else {
                        LOGGER.error("Failed to parse UpdateEvent from payload: {}", j.getPayload());
                    }
                    break;
                default:
                    LOGGER.warn("Job category {} not handled in JobFlowProducer", category);
                    break;
            }
        }
    }
}
