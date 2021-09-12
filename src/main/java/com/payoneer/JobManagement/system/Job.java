package com.payoneer.JobManagement.system;

import com.payoneer.JobManagement.api.enums.JobPriority;
import com.payoneer.JobManagement.api.response.ApiResponse;
import com.payoneer.JobManagement.api.enums.JobExecutionType;
import com.payoneer.JobManagement.api.response.ErrorResponse;
import com.payoneer.JobManagement.domain.entities.BaseJobEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * entry interface to the API, It's used to start and process jobs of various types.
 * It's not tight to any type.
 * all the T types must extend BaseJobEntity in flavor of having a valid and usable Database schema that can be followed to have an abstracted
 * API Layer. see ApiResponse for more information.
 *
 * @param <T>  type that extends the BaseJobEntity
 * @param <ID> id that extends Serializable interface
 * @author Feras E Alawadi
 * @version 1.0.101
 * @see BaseJobEntity for more information.
 * @see ApiResponse
 * @since 1.0.101
 */
public interface Job<T extends BaseJobEntity, ID extends Serializable> {

    /**
     * Method to create a new job
     *
     * @param job           the job to be executed
     * @param executionType the execution type.
     * @param jobPriority its priority
     * @return API Response.
     */
    ApiResponse<T, ErrorResponse> createJob(T job, JobExecutionType executionType, JobPriority jobPriority);

    /**
     * method to execute a job and can be chained with  other methods to perform the task.
     * this method can be chained with other methods in flavor of Fluent Apis.
     * the data will be stored in both local queue and an RDBMS System MySQL, POSTGRES, h2...etc.
     *
     * @param job  the job to be executed.
     * @param when to schedule the task in a future time.
     * @return apr response.
     */
        Job<T, ID> createJob(T job, LocalDateTime when);

    /**
     * method to schedule a task
     * the approach is straightforward, init the object then call createJob(param, param) method to init the lib.
     * after that the job can be scheduled in here, while the job can be executed directly.
     * we also have the ability to queues it from future execution.
     * the data will be stored in both local queue and an RDBMS System MySQL, POSTGRES, h2...etc.
     *
     * @param executionType the execution type.
     * @return API Response.
     * @param jobPriority its priority
     * @see JobExecutionType from more info about execution types.
     */
    ApiResponse<T, ErrorResponse> scheduleJob(JobExecutionType executionType, JobPriority jobPriority);

    /**
     * job that already have been submitted to the queue. can be canceled.
     * the theory in here is if the task is still available in the queue it could be canceled :)
     * we'll never clear it from our warehouse for future reference.
     *
     * @param jobId the job to be deleted :)
     * @return API Response
     */
    ApiResponse<T, ErrorResponse> cancelJob(ID jobId);

    /**
     * a job can be easily updated,
     * the theory in this function is that a job that have been executed and saved to a warehouse can be updated for future
     * reference.
     *
     * @param modifiedJob updated job POJO
     * @param jobId       job id
     * @return API Response
     */
    ApiResponse<T, ErrorResponse> updateJobStatus(T modifiedJob, ID jobId);

}
