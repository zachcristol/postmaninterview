package com.postman.planetexpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;

/** JPA entity representing a shipment in transit between two locations. */
@Entity
@Getter
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    protected Shipment() {
    }

    public Shipment(String origin, String destination, String status) {
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.createdAt = Instant.now();
    }
}
