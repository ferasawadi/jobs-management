package com.payoneer.JobManagement.v1;

import com.payoneer.JobManagement.api.enums.JobExecutionType;
import com.payoneer.JobManagement.domain.repos.BaseJobRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepo extends BaseJobRepo<EmailJob, UUID> {
    List<EmailJob> findAllByExecutionType(JobExecutionType executionType);
}
