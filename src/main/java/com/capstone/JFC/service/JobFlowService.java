package com.capstone.JFC.service;

import org.springframework.stereotype.Service;

import com.capstone.JFC.model.CategoryConcurrencyConfig;
import com.capstone.JFC.model.Job;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.JobStatus;
import com.capstone.JFC.model.TenantConcurrencyConfig;
import com.capstone.JFC.repository.CategoryConcurrencyConfigRepository;
import com.capstone.JFC.repository.JobRepository;
import com.capstone.JFC.repository.TenantConcurrencyConfigRepository;

import java.util.*;
import java.time.LocalDateTime;

@Service
public class JobFlowService {

    private final JobRepository jobRepository;
    private final CategoryConcurrencyConfigRepository concurrencyConfigRepository;
    private final TenantConcurrencyConfigRepository tenantConcurrencyConfigRepository;

    public JobFlowService(JobRepository jobRepository,
                          CategoryConcurrencyConfigRepository concurrencyConfigRepository, TenantConcurrencyConfigRepository tenantConcurrencyConfigRepository) {
        this.jobRepository = jobRepository;
        this.concurrencyConfigRepository = concurrencyConfigRepository;
        this.tenantConcurrencyConfigRepository = tenantConcurrencyConfigRepository;
    }

    private int getJobTypeLimit(String jobTypeKey) {
        return concurrencyConfigRepository.findByConfigKey(jobTypeKey)
                .map(CategoryConcurrencyConfig::getConfigValue)
                .orElseThrow(() -> new IllegalStateException("Concurrency configuration for job type key '" + jobTypeKey + "' not found.")); 
    }

    private int getTenantLimit(String categoryKey, String tenantKey) {
        return tenantConcurrencyConfigRepository.findByCategoryConfigKeyAndTenantConfigKey(categoryKey, tenantKey)
                .map(TenantConcurrencyConfig::getConfigValue)
                .orElseThrow(() -> new IllegalStateException("Concurrency configuration for tenant key '" + tenantKey + "' not found."));

        // return concurrencyConfigRepository.findByConfigKey(tenantKey)
        //         .map(CategoryConcurrencyConfig::getConfigValue)
        //         .orElseThrow(() -> new IllegalStateException("Concurrency configuration for tenant key '" + tenantKey + "' not found."));
    }

    public void createNewJob(String jobId, JobCategory category, String tenantId, String payload, String destinationTopic) {
        Job job = new Job(
            jobId,
            category,
            tenantId,
            payload,
            JobStatus.NEW,
            LocalDateTime.now(),
            LocalDateTime.now(),
            destinationTopic
        );
        jobRepository.save(job);
    }

    public List<Job> scheduleJobs(JobCategory category) {
        String jobTypeKey = category.name();
        int globalLimit = getJobTypeLimit(jobTypeKey);

        long inProgressCount = jobRepository.countByStatusAndJobCategory(JobStatus.IN_PROGRESS, category);
        int availableSlots = globalLimit - (int)inProgressCount;
        if (availableSlots <= 0) {
            return Collections.emptyList();
        }

        List<Job> newJobs = jobRepository.findByStatusAndJobCategory(JobStatus.NEW, category);
        if (newJobs.isEmpty()) return new ArrayList<>();

        List<Job> scheduled = new ArrayList<>();
        Map<String, Long> tenantInProgressMap = new HashMap<>();

        for (Job j : newJobs) {
            if (scheduled.size() >= availableSlots) break;

            long tenantCount = jobRepository.countByStatusAndJobCategoryAndTenantId(JobStatus.IN_PROGRESS, category, j.getTenantId());
            tenantInProgressMap.putIfAbsent(j.getTenantId(), tenantCount);

            int tenantLimit = getTenantLimit(jobTypeKey, j.getTenantId());
            if (tenantInProgressMap.get(j.getTenantId()) < tenantLimit) {
                j.setStatus(JobStatus.READY);
                j.setTimestampUpdated(LocalDateTime.now());
                jobRepository.save(j);
                scheduled.add(j);

                tenantInProgressMap.put(j.getTenantId(), tenantInProgressMap.get(j.getTenantId()) + 1);
            }
        }
        return scheduled;
    }

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
}
