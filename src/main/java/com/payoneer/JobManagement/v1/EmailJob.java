package com.payoneer.JobManagement.v1;

import com.payoneer.JobManagement.api.enums.JobExecutionType;
import com.payoneer.JobManagement.api.enums.JobPriority;
import com.payoneer.JobManagement.domain.entities.BaseJobEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "emails")
public class EmailJob extends BaseJobEntity {
    private String to;
    private String subject;
    private String name;
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "execution_type")
    private JobExecutionType executionType;
    @Enumerated(EnumType.STRING)
    @Column(name = "job_priority")
    private JobPriority jobPriority;

    public EmailJob() {

    }

    public EmailJob(UUID id, Instant created, String to, String subject, String name, String content, JobExecutionType executionType, JobPriority jobPriority) {
        super(id, created);
        this.to = to;
        this.name = name;
        this.content = content;
        this.subject = subject;
        this.executionType = executionType;
        this.jobPriority = jobPriority;
    }


    public JobPriority getJobPriority() {
        return jobPriority;
    }

    public void setJobPriority(JobPriority jobPriority) {
        this.jobPriority = jobPriority;
    }

    public JobExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(JobExecutionType executionType) {
        this.executionType = executionType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EmailJob{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", to='" + to + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
