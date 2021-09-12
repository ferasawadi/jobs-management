package com.payoneer.JobManagement.v1;

import com.payoneer.JobManagement.api.enums.ApiResponseCodes;
import com.payoneer.JobManagement.api.enums.JobExecutionType;
import com.payoneer.JobManagement.api.request.CreateEmailJobRequest;
import com.payoneer.JobManagement.api.request.ScheduleTaskReq;
import com.payoneer.JobManagement.api.response.ApiResponse;
import com.payoneer.JobManagement.api.response.ErrorResponse;
import com.payoneer.JobManagement.api.utils.ApiUtils;
import com.payoneer.JobManagement.system.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * controller to handle Job Operation for this it's designed for Email Job
 * and build on top of the system API
 * please check the following files to see what it contains.
 * a technical documentation will be provided along with those files to describe the process.
 * please keep in mind that a tech docs will be available in the README.md file as well.
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
@RestController
@RequestMapping("api/v1/job")
public class JobsController {

    private final Job<EmailJob, UUID> jobService;
    private final EmailRepo emailRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsController.class);

    public JobsController(Job<EmailJob, UUID> jobService, EmailRepo emailRepo) {
        this.jobService = jobService;
        this.emailRepo = emailRepo;
    }

    @PostMapping
    @Async
    public CompletableFuture<ApiResponse<EmailJob, ErrorResponse>> create(@RequestBody CreateEmailJobRequest createEmailJobRequest) {
        LOGGER.info(" [.] calling Email Job Create Service: {} ", createEmailJobRequest);


        // mapping the execution type in case of failure.
        JobExecutionType executionType;
        try {
            executionType = JobExecutionType.valueOf(createEmailJobRequest.getExecutionType());
        } catch (Exception e) {
            executionType = JobExecutionType.EXECUTE;
        }
        // UUID id, Instant created, String to, String subject, String name, String content
        var emailJob = new EmailJob(
                UUID.randomUUID(),
                Instant.now(),
                createEmailJobRequest.getEmailJob().getTo(),
                createEmailJobRequest.getEmailJob().getSubject(),
                createEmailJobRequest.getEmailJob().getName(),
                createEmailJobRequest.getEmailJob().getContent(),
                executionType
        );

        return CompletableFuture.completedFuture(
                jobService.createJob(emailJob, executionType)
        );
    }


    @PostMapping(value = "/sc")
    @Async
    public CompletableFuture<ApiResponse<EmailJob, ErrorResponse>> schedule(@RequestBody ScheduleTaskReq scheduleTaskReq) {
        LOGGER.info(" [.] executing task: {} ", scheduleTaskReq);


        // mapping the execution type in case of failure.
        JobExecutionType executionType;
        try {
            executionType = JobExecutionType.valueOf(scheduleTaskReq.getExecutionType());
        } catch (Exception e) {
            executionType = JobExecutionType.EXECUTE;
        }

        EmailJob emailJob = new EmailJob(
                UUID.randomUUID(),
                Instant.now(),
                scheduleTaskReq.getEmailJob().getTo(),
                scheduleTaskReq.getEmailJob().getSubject(),
                scheduleTaskReq.getEmailJob().getName(),
                scheduleTaskReq.getEmailJob().getContent(),
                executionType
        );
        return CompletableFuture.completedFuture(
                jobService.createJob(emailJob, scheduleTaskReq.getWhen()).scheduleJob(executionType)
        );
    }

    @DeleteMapping
    @Async
    public CompletableFuture<ApiResponse<EmailJob, ErrorResponse>> cancelJob(@RequestParam("id") String uuid) {
        if (!ApiUtils.validUUID(uuid))
            return CompletableFuture.completedFuture(ApiUtils.errorResponse(false, "Can't Create a Job", ApiResponseCodes.FAIL.name(),
                    new ErrorResponse("that is not a valid is", ApiResponseCodes.FAIL.name())));

        return CompletableFuture.completedFuture(jobService.cancelJob(UUID.fromString(uuid)));
    }

    @GetMapping
    public CompletableFuture<ApiResponse<List<EmailJob>, ErrorResponse>> getAll() {
        List<EmailJob> emailJobList = emailRepo.findAll();
        if (emailJobList.isEmpty())
            return CompletableFuture.completedFuture(
                    ApiUtils.errorResponse(false, "can't load data", ApiResponseCodes.TABLE_EMPTY.name(),
                            new ErrorResponse("email jobs table already empty", ApiResponseCodes.TABLE_EMPTY.name()))
            );


        return CompletableFuture.completedFuture(
                ApiUtils.success(true, "data loaded successfully!", ApiResponseCodes.SUCCESS.name(), emailJobList)
        );
    }

    // this one can be improved to be flexible when people send criteria to search for data.
    // i will keep it simple now.
    @GetMapping(value = "/query")
    @Async
    public CompletableFuture<ApiResponse<List<EmailJob>, ErrorResponse>> searchEmailJobs(@RequestParam("execution_type") String executionType) {

        ///todo for now we are matching only the values in the ENUM CLASS but the system must be extended to even check new types
        //  by getting them from the properties file or a warehouse.

        LOGGER.info("[.] query data for: {}", executionType);
        // mapping the execution type in case of failure.
        JobExecutionType type;
        try {
            type = JobExecutionType.valueOf(executionType.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    ApiUtils.errorResponse(false, "that's not a valid execution type", ApiResponseCodes.FAIL.name(),
                            new ErrorResponse("can't match execution type", ApiResponseCodes.FAIL.name()))
            );
        }
        List<EmailJob> emailJobList = emailRepo.findAllByExecutionType(type);
        return emailJobList.isEmpty() ?
                CompletableFuture.completedFuture(
                        ApiUtils.errorResponse(false, "can't load data", ApiResponseCodes.FAIL.name(),
                                new ErrorResponse("either the table is empty or can't match data", ApiResponseCodes.FAIL.name())))
                : CompletableFuture.completedFuture(
                ApiUtils.success(true, "data loaded successfully", ApiResponseCodes.SUCCESS.name(),
                        emailJobList)
        );
    }
}
