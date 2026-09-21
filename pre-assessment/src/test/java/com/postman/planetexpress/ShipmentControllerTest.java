package com.postman.planetexpress;

import static org.assertj.core.api.Assertions.assertThat;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.repository.ShipmentRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShipmentControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void postCreatesADraftShipmentForTheDestination() {
        ResponseEntity<Shipment> response = restTemplate.postForEntity(
                "/api/shipments", Map.of("destinationId", "dst_mars"), Shipment.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Shipment created = response.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).matches("shp_[a-z0-9]{6}");
        assertThat(response.getHeaders().getLocation()).hasPath("/api/shipments/" + created.getId());

        Shipment stored = shipmentRepository.findById(created.getId()).orElseThrow();
        assertThat(stored.getDestinationId()).isEqualTo("dst_mars");
        assertThat(stored.getStatus()).isEqualTo("draft");
    }

    @Test
    void postWithoutADestinationIsRejected() {
        ResponseEntity<String> missing = restTemplate.postForEntity(
                "/api/shipments", Map.of(), String.class);
        ResponseEntity<String> blank = restTemplate.postForEntity(
                "/api/shipments", Map.of("destinationId", "  "), String.class);

        assertThat(missing.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(blank.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getListsCreatedShipments() {
        Shipment created = restTemplate.postForEntity(
                "/api/shipments", Map.of("destinationId", "dst_venus"), Shipment.class).getBody();

        ResponseEntity<Shipment[]> response = restTemplate.getForEntity("/api/shipments", Shipment[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(List.of(response.getBody()))
                .anySatisfy(s -> {
                    assertThat(s.getId()).isEqualTo(created.getId());
                    assertThat(s.getDestinationId()).isEqualTo("dst_venus");
                });
    }
}
