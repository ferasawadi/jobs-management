package com.payoneer.JobManagement.api.configs;

import com.payoneer.JobManagement.system.JobService;
import com.payoneer.JobManagement.v1.EmailJob;
import com.payoneer.JobManagement.v1.EmailRepo;
import com.payoneer.JobManagement.v1.PrintingEntity;
import com.payoneer.JobManagement.v1.PrintingRepo;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.UUID;

/**
 * configuration class used to have global system configuration
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
@Configuration
public class ApiConfigs {

    /**
     * bean to create new instance of the Job Implementation
     *
     * @param emailRepo pojo extends BaseJobEntity
     * @return new instance of the API
     * @see com.payoneer.JobManagement.domain.entities.BaseJobEntity
     * @see com.payoneer.JobManagement.domain.repos.BaseJobRepo
     */
    @Bean
    public JobService<EmailJob, UUID> jobService(EmailRepo emailRepo) {
        return new JobService<>(emailRepo);
    }

    /**
     * bean to create new instance of the Job Implementation
     *
     * @param printingRepo pojo extends BaseJobEntity
     * @return new instance for printing job
     * @see com.payoneer.JobManagement.domain.entities.BaseJobEntity
     * @see com.payoneer.JobManagement.domain.repos.BaseJobRepo
     */
    @Bean
    public JobService<PrintingEntity, UUID> printingEntityUUIDJobService(PrintingRepo printingRepo) {
        return new JobService<>(printingRepo);
    }

}
