package com.capstone.JFC.service;

import org.springframework.stereotype.Service;

import com.capstone.JFC.model.ConcurrencyConfig;
import com.capstone.JFC.model.Job;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.JobStatus;
import com.capstone.JFC.repository.ConcurrencyConfigRepository;
import com.capstone.JFC.repository.JobRepository;

import java.util.*;
import java.time.LocalDateTime;

@Service
public class JobFlowService {

    private final JobRepository jobRepository;
    private final ConcurrencyConfigRepository concurrencyConfigRepository;

    public JobFlowService(JobRepository jobRepository,
                          ConcurrencyConfigRepository concurrencyConfigRepository) {
        this.jobRepository = jobRepository;
        this.concurrencyConfigRepository = concurrencyConfigRepository;
    }

    private int getJobTypeLimit(String jobTypeKey) {
        return concurrencyConfigRepository.findByConfigKey(jobTypeKey)
                .map(ConcurrencyConfig::getConfigValue)
                .orElse(5); // default to 5 if not found
    }

    // Loads the concurrency limit for a specific tenant from DB.
    private int getTenantLimit(String tenantKey) {
        return concurrencyConfigRepository.findByConfigKey(tenantKey)
                .map(ConcurrencyConfig::getConfigValue)
                .orElse(2); // default to 2 if not found
    }


    // Save new job
    public void createNewJob(String jobId, JobCategory category, String tenantId, String payload) {
        Job job = new Job(
            jobId,
            category,
            tenantId,
            payload,
            JobStatus.NEW,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        jobRepository.save(job);
    }

    // This method tries to schedule jobs of a particular category from NEW to READY, and then sets them to IN_PROGRESS
    // and publishes them. We'll do the actual publishing in the calling code (or pass a callback).
    public List<Job> scheduleJobs(JobCategory category) {
        // 1) concurrency limit for the job type
        String jobTypeKey = category.name();
        int globalLimit = getJobTypeLimit(jobTypeKey);

        // 2) how many are currently in progress
        long inProgressCount = jobRepository.countByStatusAndJobCategory(JobStatus.IN_PROGRESS, category);
        int availableSlots = globalLimit - (int)inProgressCount;
        if (availableSlots <= 0) {
            return Collections.emptyList();
        }

        // 3) Grab NEW jobs for this category
        List<Job> newJobs = jobRepository.findByStatusAndJobCategory(JobStatus.NEW, category);
        if (newJobs.isEmpty()) return new ArrayList<>();

        // 4) We'll filter them by tenant concurrency
        List<Job> scheduled = new ArrayList<>();
        Map<String, Long> tenantInProgressMap = new HashMap<>();

        for (Job j : newJobs) {
            if (scheduled.size() >= availableSlots) break;

            // check how many are in progress for this tenant
            long tenantCount = jobRepository.countByStatusAndJobCategoryAndTenantId(JobStatus.IN_PROGRESS, category, j.getTenantId());
            tenantInProgressMap.putIfAbsent(j.getTenantId(), tenantCount);

            int tenantLimit = getTenantLimit(j.getTenantId());
            if (tenantInProgressMap.get(j.getTenantId()) < tenantLimit) {
                // schedule
                j.setStatus(JobStatus.READY);
                j.setTimestampUpdated(LocalDateTime.now());
                jobRepository.save(j);
                scheduled.add(j);

                // occupant
                tenantInProgressMap.put(j.getTenantId(), tenantInProgressMap.get(j.getTenantId()) + 1);
            }
        }
        return scheduled;
    }

    // mark jobs as in-progress (once we publish them)
    public void markJobsInProgress(List<Job> jobs) {
        for (Job j : jobs) {
            j.setStatus(JobStatus.IN_PROGRESS);
            j.setTimestampUpdated(LocalDateTime.now());
            jobRepository.save(j);
        }
    }

    public void markJobAsSuccess(String jobId) {
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(JobStatus.SUCCESS);
            job.setTimestampUpdated(LocalDateTime.now());
            jobRepository.save(job);
        });
    }

    public void markJobAsFailure(String jobId) {
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(JobStatus.FAILURE);
            job.setTimestampUpdated(LocalDateTime.now());
            jobRepository.save(job);
        });
    }

    // etc. ...
}
