package com.payoneer.JobManagement.v1;

import com.payoneer.JobManagement.domain.entities.BaseJobEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "printing")
public class PrintingEntity extends BaseJobEntity {
    private String content;


    public PrintingEntity() {
    }

    public PrintingEntity(UUID id, Instant created, String content) {
        super(id, created);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PrintingEntity{" +
                "id=" + id +
                ", version=" + version +
                ", content='" + content + '\'' +
                '}';
    }
}
