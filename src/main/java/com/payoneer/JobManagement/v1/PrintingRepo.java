package com.payoneer.JobManagement.v1;

import com.payoneer.JobManagement.domain.repos.BaseJobRepo;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrintingRepo extends BaseJobRepo<PrintingEntity, UUID> {
}
