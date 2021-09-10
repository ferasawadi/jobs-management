package com.payoneer.JobManagement.api.request;

import java.time.LocalDateTime;

public class ScheduleTaskReq {
    private LocalDateTime when;
    private EmailReq emailJob;
    private String executionType;


    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

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
        return "ScheduleTaskReq{" +
                "when='" + when + '\'' +
                ", emailReq=" + emailJob +
                ", executionType='" + executionType + '\'' +
                '}';
    }
}
