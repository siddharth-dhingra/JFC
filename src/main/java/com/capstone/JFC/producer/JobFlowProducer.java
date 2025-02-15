package com.capstone.JFC.producer;

import com.capstone.JFC.dto.ScanParseEvent;
import com.capstone.JFC.dto.ScanRequestEvent;
import com.capstone.JFC.model.FileLocationEvent;
import com.capstone.JFC.model.Job;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.ScanEvent;
import com.capstone.JFC.model.ToolType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobFlowProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobFlowProducer.class);

    @Value("${app.kafka.topics.jfc-tool}")
    private String scanPullTopic;

    @Value("${app.kafka.topics.jfc-parser}")
    private String scanParseTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public JobFlowProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishScheduledJobs(JobCategory category, List<Job> jobs) {
        for (Job j : jobs) {
            switch (category) {
                case SCAN_PULL_CODESCAN:
                case SCAN_PULL_DEPENDABOT:
                case SCAN_PULL_SECRETSCAN:
                    ScanEvent scanEvent = parseScanEvent(j.getPayload());
                    if (scanEvent != null) {
                        publishScanPullEvent(j.getJobId(), scanEvent);
                    } else {
                        LOGGER.error("Failed to parse ScanEvent from payload: {}", j.getPayload());
                    }
                    break;
                case SCAN_PARSE_CODESCAN:
                case SCAN_PARSE_DEPENDABOT:
                case SCAN_PARSE_SECRETSCAN:
                    FileLocationEvent fle = parseFileLocationEvent(j.getPayload());
                    if (fle != null) {
                        publishScanParseEvent(j.getJobId(), fle);
                    } else {
                        LOGGER.error("Failed to parse FileLocationEvent from payload: {}", j.getPayload());
                    }
                    break;
                default:
                    LOGGER.warn("Job category {} not handled in JobFlowProducer", category);
                    break;
            }
        }
    }

    private void publishScanPullEvent(String jobId, ScanEvent payload) {
        // Wrap the payload in a ScanRequestEvent with the jobId as eventId.
        ScanRequestEvent event = new ScanRequestEvent(payload, jobId);
        kafkaTemplate.send(scanPullTopic, event);
        LOGGER.info("Published ScanRequestEvent to topic {}: {}", scanPullTopic, event);
    }

    private void publishScanParseEvent(String jobId, FileLocationEvent payload) {
        // Wrap the payload in a ScanParseEvent with the jobId.
        ScanParseEvent event = new ScanParseEvent(jobId, payload);
        kafkaTemplate.send(scanParseTopic, event);
        LOGGER.info("Published ScanParseEvent to topic {}: {}", scanParseTopic, event);
    }

    private ScanEvent parseScanEvent(String payload) {
        if (payload != null && payload.startsWith("ScanEvent{")) {
            try {
                // Expected format: "ScanEvent{tenantId='1', toolType=SECRETSCAN}"
                String content = payload.substring(payload.indexOf("{") + 1, payload.lastIndexOf("}"));
                String[] parts = content.split(",");
                String tenantId = null;
                ToolType toolType = null;
                for (String part : parts) {
                    part = part.trim();
                    if (part.startsWith("tenantId=")) {
                        tenantId = part.split("=")[1].replace("'", "").trim();
                    } else if (part.startsWith("toolType=")) {
                        String toolStr = part.split("=")[1].trim();
                        toolType = ToolType.valueOf(toolStr);
                    }
                }
                return new ScanEvent(tenantId, toolType);
            } catch (Exception e) {
                LOGGER.error("Error manually parsing ScanEvent from payload: {}", payload, e);
                return null;
            }
        } else {
            try {
                return objectMapper.readValue(payload, ScanEvent.class);
            } catch (Exception e) {
                LOGGER.error("Error parsing ScanEvent from payload using JSON: {}", payload, e);
                return null;
            }
        }
    }

    /**
     * Deserialize the payload string into a FileLocationEvent.
     * If the payload appears as a toString() output, manually parse the values.
     */
    private FileLocationEvent parseFileLocationEvent(String payload) {
        if (payload != null && payload.startsWith("FileLocationEvent{")) {
            try {
                // Expected format: "FileLocationEvent{tenantId='1', filePath='/path/to/file', toolName=CODESCAN}"
                String content = payload.substring(payload.indexOf("{") + 1, payload.lastIndexOf("}"));
                String[] parts = content.split(",");
                String tenantId = null;
                String filePath = null;
                ToolType toolType = null;
                for (String part : parts) {
                    part = part.trim();
                    if (part.startsWith("tenantId=")) {
                        tenantId = part.split("=")[1].replace("'", "").trim();
                    } else if (part.startsWith("filePath=")) {
                        filePath = part.split("=")[1].replace("'", "").trim();
                    } else if (part.startsWith("toolName=")) {
                        String toolStr = part.split("=")[1].replace("'", "").trim();
                        toolType = ToolType.valueOf(toolStr);
                    }
                    
                }
                return new FileLocationEvent(tenantId, filePath, toolType);
            } catch (Exception e) {
                LOGGER.error("Error manually parsing FileLocationEvent from payload: {}", payload, e);
                return null;
            }
        } else {
            try {
                return objectMapper.readValue(payload, FileLocationEvent.class);
            } catch (Exception e) {
                LOGGER.error("Error parsing FileLocationEvent from payload using JSON: {}", payload, e);
                return null;
            }
        }
    }
}
