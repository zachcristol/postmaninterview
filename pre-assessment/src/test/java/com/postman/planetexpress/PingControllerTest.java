package com.postman.planetexpress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Real Postgres container + real Flyway run, not H2: this is the same reasoning as
// ShipmentRepositoryTest - a Postgres-specific mapping shouldn't be verified against H2.
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PingControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void healthEndpointReportsUp() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }

    @Test
    void pingSeedsExactlyOneShipmentAndStaysIdempotent() {
        ResponseEntity<String> first = restTemplate.getForEntity("/api/ping", String.class);
        assertThat(first.getBody()).isEqualTo("{\"status\":\"ok\",\"database\":\"postgres\",\"shipmentCount\":1}");

        ResponseEntity<String> second = restTemplate.getForEntity("/api/ping", String.class);
        assertThat(second.getBody()).isEqualTo("{\"status\":\"ok\",\"database\":\"postgres\",\"shipmentCount\":1}");
    }
}
