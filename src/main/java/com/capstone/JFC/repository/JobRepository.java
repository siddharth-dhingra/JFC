package com.capstone.JFC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.JFC.model.Job;
import com.capstone.JFC.model.JobCategory;
import com.capstone.JFC.model.JobStatus;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findByStatusAndJobCategory(JobStatus status, JobCategory jobCategory);
    long countByStatusAndJobCategory(JobStatus status, JobCategory jobCategory);
    long countByStatusAndJobCategoryAndTenantId(JobStatus status, JobCategory jobCategory, String tenantId);
}
