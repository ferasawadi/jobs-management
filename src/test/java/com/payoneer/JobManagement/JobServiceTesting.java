package com.payoneer.JobManagement;

import com.payoneer.JobManagement.api.enums.JobPriority;
import com.payoneer.JobManagement.api.response.ApiResponse;
import com.payoneer.JobManagement.api.enums.JobExecutionType;
import com.payoneer.JobManagement.api.response.ErrorResponse;
import com.payoneer.JobManagement.system.Job;
import com.payoneer.JobManagement.system.JobUpdated;
import com.payoneer.JobManagement.v1.EmailJob;
import com.payoneer.JobManagement.v1.EmailRepo;
import com.payoneer.JobManagement.v1.PrintingEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.*;
import java.time.Instant;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobServiceTesting {


    @Autowired
    private Job<EmailJob, UUID> jobService;

    // future demonstration for updated system
    // private JobUpdated<PrintingEntity, UUID> jobUpdated;
    @Autowired
    private Job<PrintingEntity, UUID> printingService;

    @Autowired
    private EmailRepo emailRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceTesting.class);

    @Test
    public void createJobTest() {
        // UUID id, Instant created , String to, String name, String content
        EmailJob emailJob = new EmailJob(
                UUID.randomUUID(),
                Instant.now(),
                "ferasawady@gmail.com",
                "Email Job Test",
                "Feras Alawadi",
                "this is email Job to be Executed...",
                JobExecutionType.EXECUTE,
                JobPriority.MEDIUM
        );

        // save to warehouse for future reference.
//        emailRepo.save(emailJob);

        ApiResponse<EmailJob, ErrorResponse> response = jobService.createJob(emailJob, JobExecutionType.EXECUTE, JobPriority.MEDIUM);
        ApiResponse<EmailJob, ErrorResponse> response1 = jobService.createJob(emailJob, JobExecutionType.QUEUED,  JobPriority.MEDIUM);
        ApiResponse<EmailJob, ErrorResponse> response2 = jobService.createJob(emailJob, JobExecutionType.UNKNOWN,  JobPriority.MEDIUM);

        LOGGER.info(" [.] Testing results1: {}", response);
        LOGGER.info(" [.] Testing results2: {}", response1);
        LOGGER.info(" [.] Testing results3: {}", response2);

    }

    @Test
    public void TestPrintingJob() {
        // UUID id, Instant created, String content
        PrintingEntity printingEntity = new PrintingEntity(
                UUID.randomUUID(),
                Instant.now(),
                "body to print"
        );

        ApiResponse<PrintingEntity, ErrorResponse> response = printingService.createJob(printingEntity, JobExecutionType.EXECUTE,  JobPriority.MEDIUM);
        ApiResponse<PrintingEntity, ErrorResponse> response1 = printingService.createJob(printingEntity, JobExecutionType.QUEUED,  JobPriority.MEDIUM);
        ApiResponse<PrintingEntity, ErrorResponse> response2 = printingService.createJob(printingEntity, JobExecutionType.UNKNOWN,  JobPriority.MEDIUM);

        LOGGER.info(" [.] Testing Results1: {}", response);
        LOGGER.info(" [.] Testing Results2: {}", response1);
        LOGGER.info(" [.] Testing Results3: {}", response2);


        LOGGER.info(" [.] try with null values");
//        ApiResponse<PrintingEntity, ErrorResponse> response3 =
//                printingService
//                        .createJob(null, "2011-01-18 00:00:00.0")
                        // will fail the first line already throws an exception. comment the upper and uncomment the lower to test the date empty case
//                        .createJob(printingEntity, "")
//                        .scheduleJob(JobExecutionType.EXECUTE);
//
//        LOGGER.info(" [x] null values passed: {}", response3);


    }

    @Test
    public void testScheduled() {
        // UUID id, Instant created, String content
//        PrintingEntity printingEntity = new PrintingEntity(
//                UUID.randomUUID(),
//                Instant.now(),
//                "body to print"
//        );
//        LOGGER.info(" [.] try schedule a task");
//        LOGGER.info(" [.] try schedule a task job: {}", printingEntity);
//        ApiResponse<PrintingEntity, ErrorResponse> response =
//                printingService
//                        .createJob(printingEntity, "2021-09-09 18:15:00.0")
//                        .scheduleJob(JobExecutionType.EXECUTE);
//
//        LOGGER.info(" [.] scheduled : {}", response);
//
//        ApiResponse<PrintingEntity, ErrorResponse> response1 =
//                printingService
//                        .createJob(printingEntity, "2021-09-09 18:15:00.0")
//                        .scheduleJob(JobExecutionType.QUEUED);
//
//        LOGGER.info(" [.] scheduled : {}", response1);
//
//        ApiResponse<PrintingEntity, ErrorResponse> response2 =
//                printingService
//                        .createJob(printingEntity, "2021-09-09 18:15:00.0")
//                        .scheduleJob(JobExecutionType.UNKNOWN);
//
//        LOGGER.info(" [.] scheduled : {}", response2);
    }

}
