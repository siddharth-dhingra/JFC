package com.capstone.JFC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.JFC.model.Job;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.producer.JobFlowProducer;

import java.util.List;

@Component
public class JobScheduler {

    @Autowired
    private JobFlowService jobFlowService;

    @Autowired
    private JobFlowProducer jobFlowProducer;

    // A simple lock object to avoid concurrency
    private final Object schedulingLock = new Object();

    // Scheduled every 30s for demonstration. Cron or dynamic intervals possible.
    @Scheduled(fixedRate = 10000)
    public void scheduleJobsPeriodically() {
        runScheduler();
    }

    // Synchronized method to avoid race conditions
    @Transactional
    public void runScheduler() {
        synchronized (schedulingLock) {
            for (JobCategory category : JobCategory.values()) {
                // 1) pick new jobs and set them READY
                List<Job> readyJobs = jobFlowService.scheduleJobs(category);
                if (readyJobs.isEmpty()) continue;

                // 2) Mark them in progress
                jobFlowService.markJobsInProgress(readyJobs);

                // 3) Publish them to Kafka
                jobFlowProducer.publishScheduledJobs(category, readyJobs);
            }
        }
    }

    // Could be called on ack
    public void runSchedulerOnAck() {
        runScheduler();
    }
}