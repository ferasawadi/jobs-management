package com.payoneer.JobManagement.api.request;


public class CreateEmailJobRequest {
    private EmailReq emailJob;
    private String executionType;
    private String jobPriority;


    public EmailReq getEmailJob() {
        return emailJob;
    }

    public void setEmailJob(EmailReq emailJob) {
        this.emailJob = emailJob;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }


    public String getJobPriority() {
        return jobPriority;
    }

    public void setJobPriority(String jobPriority) {
        this.jobPriority = jobPriority;
    }

    @Override
    public String toString() {
        return "CreateEmailJobRequest{" +
                "emailJob=" + emailJob +
                ", executionType='" + executionType + '\'' +
                ", jobPriority='" + jobPriority + '\'' +
                '}';
    }
}
