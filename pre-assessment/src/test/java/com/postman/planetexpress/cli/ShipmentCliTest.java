package com.postman.planetexpress.cli;

import static org.assertj.core.api.Assertions.assertThat;

import com.postman.planetexpress.repository.ShipmentRepository;
import com.postman.planetexpress.service.ShipmentService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("cli")
class ShipmentCliTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentService shipmentService;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private ShipmentCli cli() {
        return new ShipmentCli(shipmentService, new PrintStream(out), new PrintStream(err));
    }

    @Test
    void createsAShipmentForTheGivenDestination() {
        ShipmentCli cli = cli();
        cli.run(new DefaultApplicationArguments("create-shipment", "--destination=dst_wormulon"));

        assertThat(cli.getExitCode()).isZero();
        assertThat(shipmentRepository.findAll())
                .anyMatch(s -> s.getDestinationId().equals("dst_wormulon")
                        && s.getStatus().equals("draft")
                        && s.getId().matches("shp_[a-z0-9]{6}"));
        assertThat(out.toString()).contains("dst_wormulon");
    }

    @Test
    void rejectsAnInvalidDestinationWithoutSaving() {
        long before = shipmentRepository.count();
        ShipmentCli cli = cli();
        cli.run(new DefaultApplicationArguments("create-shipment", "--destination=Mars!"));

        assertThat(cli.getExitCode()).isEqualTo(1);
        assertThat(err.toString()).contains("Invalid destination");
        assertThat(shipmentRepository.count()).isEqualTo(before);
    }

    @Test
    void printsUsageForUnknownCommands() {
        ShipmentCli cli = cli();
        cli.run(new DefaultApplicationArguments("nope"));

        assertThat(cli.getExitCode()).isEqualTo(1);
        assertThat(err.toString()).contains("Usage:");
    }
}
