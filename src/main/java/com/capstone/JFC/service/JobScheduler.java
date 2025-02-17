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

    private final Object schedulingLock = new Object();

    @Scheduled(fixedRate = 10000)
    public void scheduleJobsPeriodically() {
        runScheduler();
    }

    @Transactional
    public void runScheduler() {
        synchronized (schedulingLock) {
            for (JobCategory category : JobCategory.values()) {
                List<Job> readyJobs = jobFlowService.scheduleJobs(category);
                if (readyJobs.isEmpty()) continue;

                jobFlowProducer.publishScheduledJobs(category, readyJobs);

                jobFlowService.markJobsInProgress(readyJobs);
            }
        }
    }

    public void runSchedulerOnAck() {
        runScheduler();
    }
}