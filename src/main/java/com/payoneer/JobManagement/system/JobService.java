package com.payoneer.JobManagement.system;

import com.payoneer.JobManagement.api.enums.JobPriority;
import com.payoneer.JobManagement.api.response.ApiResponse;
import com.payoneer.JobManagement.api.enums.ApiResponseCodes;
import com.payoneer.JobManagement.api.enums.JobExecutionType;
import com.payoneer.JobManagement.api.response.ErrorResponse;
import com.payoneer.JobManagement.api.utils.ApiUtils;
import com.payoneer.JobManagement.domain.entities.BaseJobEntity;
import com.payoneer.JobManagement.domain.repos.BaseJobRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

/**
 * Job wrapper class that is used to execute tasks.
 * a job can be anything in here, the class and its entry point interface will accept any type as long as its extends BaseJobEntity
 * for more info.
 *
 * @param <T>  Job Type that extends JobBaseEntity
 * @param <ID> id must be UUID :)
 * @author Feras E Alawadi
 * @version 1.0.101
 * @see BaseJobEntity
 * a job can be executed directly or can be sceduled for future use, and can accept any new executuion feature that will be added to the system.
 * @see JobExecutionType
 * for more info about what is the status of an exeuted job please see
 * @see com.payoneer.JobManagement.api.enums.JobStatus
 * @since 1.0.101
 */
public class JobService<T extends BaseJobEntity, ID extends Serializable> implements Job<T, ID> {
    /**
     * Can't Create event String Response
     */
    protected static final String CANT_CREATE = "can't create Job";
    /**
     * job created response
     */
    protected static final String JOB_CREATED = "job has been created";
    /**
     * job canceled response
     */
    protected static final String JOB_CANCELED = "job has been canceled";
    /**
     * job can't be canceled response
     */
    protected static final String JOB_CANT_CANCELED = "job has can't be Canceled";
    /**
     * schedule response.
     */
    protected static final String JOB_SCHEDULED = "job has been scheduled successfully";
    /**
     * job schedule fail response
     */
    protected static final String JOB_SCHEDULE_FAILED = "can't schedule a job";

    /**
     * list of execution types that will be pushed using properties file.
     * there is a fall back if we failed to read the execution types.
     *
     * @see #collectExecutionTypes() to see collected data
     * @see JobExecutionType for more info about the default types that we will fall back to
     */
    @Value("#{'${job.type.values}'.split(',')}")
    private List<String> executionTypes;   // list of available Job Execution Type.
    /**
     * local Map to hold the mapped execution types.
     */
    protected Map<String, JobExecutionType> availableJobExecutionTypes = new HashMap<>();

    /**
     * when to schedule a task. a java date object that holds a valid date
     * <p>
     * the format is: 2011-01-18T00:00:00,
     * LocalDateTime date = LocalDateTime.of(2018, 6, 17, 18, 30, 0);
     */
    private LocalDateTime when;
    /**
     * schedule the tasks by using executors service to save CPU resources.
     * if you would like to go with Timer that's another approach. it's a good one but for a modern code we will go with Executors.
     */
    private static final ScheduledExecutorService futureTask = Executors.newSingleThreadScheduledExecutor();
    //private static final Timer futureTask = new Timer();

    /**
     * local job object that will be used to map the injected object to this class
     * using the method createJob(params...);
     */
    private T job;

    /**
     * queue to hold scheduled tasks.
     * thread safe queue have been used for dealing with async APIS that will be using this class implementation.
     */
    protected Queue<T> tasks = new ConcurrentLinkedDeque<>();

    /**
     * system logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    /**
     * warehouse repository is using BaseJobEntity and UUID to build basic tables.
     *
     * @see BaseJobEntity a core entity must be used by all the Jobs.
     * @see BaseJobRepo Spring JPA repository that is used as a generic entry to High level repositories.
     */
    protected BaseJobRepo<T, ID> jobRepo;

    protected boolean isScheduled;

    /**
     * default constructor.
     * singletons can be used to save memory, but for the simplicity of this demonstration
     * we're not going to use it here.
     *
     * @param jobRepo warehouse repository.
     */
    public JobService(BaseJobRepo<T, ID> jobRepo) {
        LOGGER.info(" [*] create new job instance...");
        this.jobRepo = jobRepo;
        // map the available Job Execution Types.
        collectExecutionTypes();
    }

    /**
     * Method to create a new job, jobs that will be instantly executed.
     * for queue or schedule the tasks. check the other overloaded method passing it the job and the execution date.
     *
     * @param job           the job to be executed
     * @param executionType the execution type.
     * @return API Response.
     * @param jobPriority its priority
     * @see #createJob(BaseJobEntity, LocalDateTime)
     */
    @Override
    public ApiResponse<T, ErrorResponse> createJob(T job, JobExecutionType executionType, JobPriority jobPriority) {
        // returned response.
        return processJob(job, executionType, jobPriority);
    }

    /**
     * method to execute a job and can be chained with  other methods to perform the task.
     * this method can be chained with other methods in flavor of Fluent Apis.
     * the data will be stored in both local queue and an RDBMS System MySQL, POSTGRES, h2...etc.
     * method scheduled for future use will not be saved to system warehouse until they are executed
     * <br/> <br/> <br/> <br/>
     * ex:
     * <br/> <br/> <br/>
     * <p>
     * printingService <br/>
     * .createJob(null, LocalDateTime date = LocalDateTime.of(2018, 6, 17, 18, 30, 0)) <br/>
     * .scheduleJob(JobExecutionType.EXECUTE);
     * <br/> <br/> <br/>
     *
     * @param job  the job to be executed.
     * @param when to schedule the task in a future time.
     * @return apr response.
     */
    @Override
    public Job<T, ID> createJob(T job, LocalDateTime when) {
        // check if the job is valid.
        // job instanceof BaseJobEntity can be used, but we can replace it with null
        if (job == null)
            throw new IllegalStateException("scheduled job can't be null, make sure its valid!");

        // edge case that we don't have a valid pattern or the date is null.
        if (when == null)
            throw new IllegalStateException("schedule date is not valid");

        this.when = when;
        // set local job.
        this.job = job;
        return this;
    }

    /**
     * method to schedule a task
     * the approach is straightforward, init the object then call createJob(param, param) method to init the lib.
     * after that the job can be scheduled in here, while the job can be executed directly.
     * we also have the ability to queues it from future execution.
     * the data will be stored in both local queue and an RDBMS System MySQL, POSTGRES, h2...etc.
     * method scheduled for future use will not be saved to system warehouse until they are executed
     *
     * @param executionType the execution type.
     * @return API Response.
     * @param jobPriority its priority
     * @see JobExecutionType from more info about execution types.
     */
    @Override
    public ApiResponse<T, ErrorResponse> scheduleJob(JobExecutionType executionType, JobPriority jobPriority) {
        if (job == null)
            return ApiUtils.errorResponse(false, JOB_SCHEDULE_FAILED, ApiResponseCodes.FAIL.name(),
                    new ErrorResponse("job can't be scheduled because the job is null", ApiResponseCodes.FAIL.name()));
        if (when == null)
            return ApiUtils.errorResponse(false, JOB_SCHEDULE_FAILED, ApiResponseCodes.FAIL.name(),
                    new ErrorResponse("job can't be scheduled because the target date is not valid or null", ApiResponseCodes.FAIL.name()));


        //todo we can check if the sent time is before or the same as the current date.
        // return an error in this case.
        // but i'll keep it like this, just an idea in my mind. hope you'll read those lines.

        LOGGER.info(" [*] when date: {}", when);
        // submit to process
        processJob(job, executionType, jobPriority);
        // schedule it :)
        futureTask.schedule(() -> {
            LOGGER.info(" [.] Start To Execute Scheduled task, {} at: {}", job, when);
            // call execute the queue.
            executeScheduled();
        }, LocalDateTime.now().until(when, ChronoUnit.MINUTES), TimeUnit.MINUTES);
        return ApiUtils.success(true, JOB_SCHEDULED, ApiResponseCodes.SUCCESS.name(), job);
    }

    /**
     * job that already have been submitted to the queue. can be canceled.
     * the theory in here is if the task is still available in the queue it could be canceled :)
     * we'll never clear it from our warehouse for future reference.
     *
     * @param jobId the job to be deleted :)
     * @return API Response
     */
    @Override
    public ApiResponse<T, ErrorResponse> cancelJob(ID jobId) {
        LOGGER.info(" [.] job: {} queued for cancellation", jobId);
        // will not remove it from the warehouse for reference.
        // could be canceled from the queue.
        boolean isDeleted = tasks.removeIf(job -> job.getId().equals(jobId));
        if (isDeleted)
            LOGGER.info(" [.] job: {}  cancellation is done", jobId);
        else
            LOGGER.info(" [x] job: {} can't be canceled because it might already proceed or canceled already", jobId);

        return isDeleted
                ? ApiUtils.success(true, JOB_CANCELED, ApiResponseCodes.SUCCESS.name(), null)
                : ApiUtils.errorResponse(false, JOB_CANT_CANCELED, ApiResponseCodes.FAIL.name(),
                new ErrorResponse("job: { " + jobId + " } can't be canceled because it might already proceed or canceled already ", ApiResponseCodes.FAIL.name()));
    }

    /**
     * a job can be easily updated,
     * the theory in this function is that a job that have been executed and saved to a warehouse can be updated for future
     * reference.
     *
     * @param modifiedJob updated job POJO
     * @param jobId       job id
     * @return API Response
     */
    @Override
    public ApiResponse<T, ErrorResponse> updateJobStatus(T modifiedJob, ID jobId) {
        LOGGER.info(" [.] updating job: {}, new data: {}", jobId, modifiedJob);
        return ApiUtils.success(true, "success, will be available in the near future :)", ApiResponseCodes.SUCCESS.name(), null);
    }


    /**
     * generic method to process jobs
     *
     * @param job           the job to be executed
     * @param executionType the execution type
     * @return API response.
     */
    private ApiResponse<T, ErrorResponse> processJob(T job, JobExecutionType executionType, JobPriority jobPriority) {
        ApiResponse<T, ErrorResponse> response;
        // extract execution type from the list.
        // base case is the value sent via JobExecution Type is not valid at all
        // case two is either the job is null, or the execution type is null
        if (job == null) {
            LOGGER.info(" [x] can't resolve job object is null");
            return ApiUtils.errorResponse(false, CANT_CREATE, ApiResponseCodes.FAIL.name(),
                    new ErrorResponse("you need a valid job to be executed: current is null", ApiResponseCodes.FAIL.name()));
        }

        if (executionType == null) {

            LOGGER.info(" [x] execution type can't be null, please make sure to send a valid one see: JobExecutionType from more info ");
            return ApiUtils.errorResponse(false, CANT_CREATE, ApiResponseCodes.FAIL.name(),
                    new ErrorResponse("execution type can't be null, please make sure to send a valid one see: JobExecutionType from more info ", ApiResponseCodes.FAIL.name()));
        }

        // another fallback check.
        JobExecutionType jobExecutionType = availableJobExecutionTypes.getOrDefault(executionType.name(), JobExecutionType.EXECUTE);
        LOGGER.info(" [.] Job Priority: {} ", jobPriority.name());
        switch (jobExecutionType.name().toUpperCase(Locale.ROOT)) {
            case "EXECUTE" -> {
                LOGGER.info(" [.] Execute a Job: {}", job);
                // save data to in memory warehouse for future reference.
                saveToWareHouse(job);
                // build the response
                response = ApiUtils.success(true, JOB_CREATED, ApiResponseCodes.SUCCESS.name(), job);
            }
            case "QUEUED" -> {
                LOGGER.info(" [.] Job Queued For Execution: {}", job);
                // save data to in memory warehouse for future reference.
                saveToWareHouse(job);
                // build the response
                response = ApiUtils.success(true, JOB_CREATED, ApiResponseCodes.SUCCESS.name(), job);

                // check its priority
                if (jobPriority.equals(JobPriority.HIGH)){
                    // using offer to add to the queue.
                    tasks.offer(job);
                } else if (jobPriority.equals(JobPriority.MEDIUM)){
                    // using offer to add to the queue.
                    tasks.offer(job);
                } else {
                    // using offer to add to the queue.
                    tasks.offer(job);
                }
            }
            default -> {
                LOGGER.info(" [x] error execute the task, Execution Type: {}  is not Supported", jobExecutionType.name());
                // build the response
                response = ApiUtils.errorResponse(false, CANT_CREATE, ApiResponseCodes.FAIL.name(),
                        new ErrorResponse("job execution type: { " + jobExecutionType.name() + " } is not supported", ApiResponseCodes.FAIL.name()));
            }
        }
        return response;
    }

    /**
     * method to check if we have any tasks in the queue.
     * the task might be scheduled, or just queued for execution.
     */
    // for testing uncomment this line :)
//    @Scheduled(fixedDelay = 15000)
    public void executeScheduled() {
        for (T job : tasks) {
            ///todo check if the job is just queued or is it scheduled ??
            LOGGER.info(" [.] execute scheduled Job: {}", job);
            // clear it from the queue.
            tasks.remove(job);
        }
    }

    /**
     * method to save the job to a warehouse
     *
     * @param job the job to be saved
     */
    private void saveToWareHouse(T job) {
        T saved = jobRepo.save(job);
        LOGGER.info(" [.] success save to local warehouse: {}", saved);
    }

    /**
     * simple method to collect JobExecutionTypes
     * the theory is to have the types available in the properties file.
     * note: <b> we can have them available in a database instead and control them </b>
     *
     * @see JobExecutionType
     */
    private void collectExecutionTypes() {
        LOGGER.info(" [.] start collecting execution types");
        if (executionTypes == null || executionTypes.isEmpty()) {
            LOGGER.info(" [.] execution types available in properties file.");
            // either the values are not exist in properties file or we have an error reading them.
            // Don't Worry at least we have a fallback to an ENUM Class.
            availableJobExecutionTypes.put(JobExecutionType.EXECUTE.name(), JobExecutionType.EXECUTE);
            availableJobExecutionTypes.put(JobExecutionType.QUEUED.name(), JobExecutionType.QUEUED);
            availableJobExecutionTypes.put(JobExecutionType.UNKNOWN.name(), JobExecutionType.UNKNOWN);
        } else {
            LOGGER.info(" [x] failed to find any execution types, failing back to defaults");
            for (String value : executionTypes) {
                // make sure its upper case to match the enum :)
                availableJobExecutionTypes.put(value, JobExecutionType.valueOf(value.toUpperCase(Locale.ROOT)));
            }
        }
    }

}
