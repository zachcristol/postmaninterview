package com.postman.planetexpress.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;

/** JPA entity representing a shipment, mirroring the columns of data/shipments.csv. */
@Entity
@Getter
public class Shipment {

    @Id
    private String id;

    private String status;

    private String priority;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant dispatchedAt;

    private Instant estimatedDelivery;

    private String originPlanetId;

    private String originCity;

    private String originContinent;

    private String destinationId;

    private String routeId;

    private String permitId;

    private String customsStatus;

    private int packageCount;

    private BigDecimal declaredValueDoopDollars;

    private BigDecimal insuranceValueDoopDollars;

    private String specialInstructions;

    private String etag;

    protected Shipment() {
    }

    /** Creates a draft shipment out of New New York; the remaining columns are optional. */
    public Shipment(String id, String destinationId) {
        Instant now = Instant.now();
        this.id = id;
        this.status = "draft";
        this.priority = "standard";
        this.createdAt = now;
        this.updatedAt = now;
        this.originPlanetId = "plnt_earth";
        this.originCity = "New New York";
        this.originContinent = "North America";
        this.destinationId = destinationId;
        this.customsStatus = "not_started";
        this.packageCount = 0;
        this.declaredValueDoopDollars = BigDecimal.ZERO;
        this.insuranceValueDoopDollars = BigDecimal.ZERO;
        this.etag = "W/\"" + Long.toHexString(now.toEpochMilli()) + "\"";
    }
}
