package com.postman.planetexpress;

import static org.assertj.core.api.Assertions.assertThat;

import com.postman.planetexpress.model.Crew;
import com.postman.planetexpress.model.Job;
import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.repository.CrewRepository;
import com.postman.planetexpress.repository.JobRepository;
import com.postman.planetexpress.repository.ShipmentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Real Postgres container, not H2: the Flyway schema (timestamptz, numeric, jsonb) is
// Postgres-specific and an H2-backed test would pass even if it regressed.
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShipmentRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void roundTripsAllFieldsThroughPostgres() {
        Instant before = Instant.now().minus(1, ChronoUnit.SECONDS);
        shipmentRepository.saveAndFlush(new Shipment("shp_test01", "dst_mars"));

        Shipment reloaded = shipmentRepository.findById("shp_test01").orElseThrow();

        assertThat(reloaded.getDestinationId()).isEqualTo("dst_mars");
        assertThat(reloaded.getStatus()).isEqualTo("draft");
        assertThat(reloaded.getOriginCity()).isEqualTo("New New York");
        assertThat(reloaded.getDeclaredValueDoopDollars()).isEqualByComparingTo("0");
        assertThat(reloaded.getDispatchedAt()).isNull();
        assertThat(reloaded.getCreatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    void crewCargoCertificationsRoundTripAsAnArray() {
        crewRepository.saveAndFlush(new Crew("crw_test01", "Test Crew", "Intern", "human",
                "plnt_earth", Instant.now(), List.of("standard", "dark_matter"), 10));

        Crew reloaded = crewRepository.findById("crw_test01").orElseThrow();

        assertThat(reloaded.getCargoCertifications()).containsExactly("standard", "dark_matter");
        assertThat(reloaded.getPilotLicenseId()).isNull();
    }

    @Test
    void jobsLinkToTheirShipment() {
        shipmentRepository.saveAndFlush(new Shipment("shp_test02", "dst_mars"));
        jobRepository.saveAndFlush(new Job("job_test01", "shipment_dispatch", "shp_test02"));

        List<Job> jobs = jobRepository.findByShipmentId("shp_test02");

        assertThat(jobs).hasSize(1);
        assertThat(jobs.get(0).getStatus()).isEqualTo("pending");
        assertThat(jobs.get(0).getResultJson()).isNull();
    }
}
