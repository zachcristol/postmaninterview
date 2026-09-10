package com.postman.planetexpress;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

// Real SQLite file + real Flyway run, not H2: this is the same reasoning as
// ShipmentRepositoryTest - a SQLite-specific mapping shouldn't be verified against H2.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PingControllerTest {

    @TempDir
    static Path tempDir;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) throws IOException {
        Path dbFile = tempDir.resolve("test.db");
        registry.add("spring.datasource.url", () -> "jdbc:sqlite:" + dbFile);
    }

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
        assertThat(first.getBody()).isEqualTo("{\"status\":\"ok\",\"database\":\"sqlite\",\"shipmentCount\":1}");

        ResponseEntity<String> second = restTemplate.getForEntity("/api/ping", String.class);
        assertThat(second.getBody()).isEqualTo("{\"status\":\"ok\",\"database\":\"sqlite\",\"shipmentCount\":1}");
    }
}
