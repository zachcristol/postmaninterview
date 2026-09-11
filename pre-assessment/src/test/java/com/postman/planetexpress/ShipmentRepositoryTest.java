package com.postman.planetexpress;

import static org.assertj.core.api.Assertions.assertThat;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.repository.ShipmentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Real Postgres container, not H2: the point of the identity-column id mapping in
// Shipment.java is a Postgres-specific behavior that an H2-backed test would pass
// even if it regressed.
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShipmentRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void savingAShipmentAssignsAGeneratedId() {
        Shipment saved = shipmentRepository.save(new Shipment("Earth", "Mars", "IN_TRANSIT"));

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void roundTripsAllFieldsThroughPostgres() {
        Instant before = Instant.now().minus(1, ChronoUnit.SECONDS);
        Shipment saved = shipmentRepository.save(new Shipment("Earth", "Mars", "IN_TRANSIT"));

        Shipment reloaded = shipmentRepository.findById(saved.getId()).orElseThrow();

        assertThat(reloaded.getOrigin()).isEqualTo("Earth");
        assertThat(reloaded.getDestination()).isEqualTo("Mars");
        assertThat(reloaded.getStatus()).isEqualTo("IN_TRANSIT");
        assertThat(reloaded.getCreatedAt()).isAfterOrEqualTo(before);
    }
}
