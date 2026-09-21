package com.postman.planetexpress.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA entity representing an async job against a shipment, mirroring data/jobs.csv. */
@Entity
@Getter
public class Job {

    @Id
    private String id;

    private String jobType;

    private String status;

    private String shipmentId;

    private String packageId;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant completedAt;

    private String progressCurrentStep;

    private Short progressStepsComplete;

    private Short progressStepsTotal;

    @JdbcTypeCode(SqlTypes.JSON)
    private String progressStepsJson;

    @JdbcTypeCode(SqlTypes.JSON)
    private String resultJson;

    private String errorCode;

    private String errorMessage;

    protected Job() {
    }

    /** Creates a pending job; progress, result and error fields start empty. */
    public Job(String id, String jobType, String shipmentId) {
        Instant now = Instant.now();
        this.id = id;
        this.jobType = jobType;
        this.status = "pending";
        this.shipmentId = shipmentId;
        this.createdAt = now;
        this.updatedAt = now;
    }
}
