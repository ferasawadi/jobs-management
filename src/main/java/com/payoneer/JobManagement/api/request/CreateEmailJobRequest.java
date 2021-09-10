package com.payoneer.JobManagement.api.request;


public class CreateEmailJobRequest {
    private EmailReq emailJob;
    private String executionType;


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

    @Override
    public String toString() {
        return "CreateEmailJobRequest{" +
                "emailJob=" + emailJob +
                ", executionType='" + executionType + '\'' +
                '}';
    }
}
