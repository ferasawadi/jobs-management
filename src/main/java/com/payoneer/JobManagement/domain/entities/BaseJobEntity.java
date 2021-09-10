package com.payoneer.JobManagement.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Core WareHouse, POJO Entity class. superclass for all other classes that intend to use the API,
 * it contains the general shape of any table contains the id and dates objects.
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseJobEntity {
    /**
     * to used RDMS System Please UnComment the lines and comment the active ones.
     * H2 Database need a different type support fo UUIDs.
     */
    @Id
    // uncomment to use RDMS like MYSQL
    //@Type(type = "org.hibernate.type.UUIDCharType")
    // uncomment to use RDMS like MYSQL
    // @Column(name = "id", columnDefinition = "BINARY(36)", updatable = false, nullable = false)
    // comment to use RDMS like MYSQL
    @Column(name = "id")
    // comment to use RDMS like MYSQL
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    protected UUID id;

    /**
     * resource create Date
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonSerialize(using = InstantSerializer.class)
    private Instant created;

    /**
     * resource modification Date
     */
    @LastModifiedDate
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonSerialize(using = InstantSerializer.class)
    private Instant modified;

    /**
     * our API Version
     */
    @Version
    protected int version = 1;


    public BaseJobEntity(UUID id, Instant created) {
        this.id = id;
        this.created = created;
    }

    public BaseJobEntity() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BaseJobEntity{" +
                "id=" + id +
                ", created=" + created +
                ", modified=" + modified +
                ", version='" + version + '\'' +
                '}';
    }
}
