package com.postman.planetexpress;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

// Real SQLite file, not H2: the point of the INTEGER id mapping in Shipment.java is a
// SQLite-specific quirk that an H2-backed test would pass even if it regressed.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShipmentRepositoryTest {

    @TempDir
    static Path tempDir;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) throws IOException {
        Path dbFile = tempDir.resolve("test.db");
        registry.add("spring.datasource.url", () -> "jdbc:sqlite:" + dbFile);
    }

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void savingAShipmentAssignsAGeneratedId() {
        Shipment saved = shipmentRepository.save(new Shipment("Earth", "Mars", "IN_TRANSIT"));

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void roundTripsAllFieldsThroughSqlite() {
        Instant before = Instant.now().minus(1, ChronoUnit.SECONDS);
        Shipment saved = shipmentRepository.save(new Shipment("Earth", "Mars", "IN_TRANSIT"));

        Shipment reloaded = shipmentRepository.findById(saved.getId()).orElseThrow();

        assertThat(reloaded.getOrigin()).isEqualTo("Earth");
        assertThat(reloaded.getDestination()).isEqualTo("Mars");
        assertThat(reloaded.getStatus()).isEqualTo("IN_TRANSIT");
        assertThat(reloaded.getCreatedAt()).isAfterOrEqualTo(before);
    }
}
