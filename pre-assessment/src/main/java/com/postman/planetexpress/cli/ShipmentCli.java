package com.postman.planetexpress.cli;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.service.ShipmentService;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Command-line interface, active only under the {@code cli} profile so the web service
 * never runs it. Usage: {@code create-shipment [--destination=dst_mars]}; when the
 * destination is omitted it is read from stdin.
 */
@Component
@Profile("cli")
public class ShipmentCli implements ApplicationRunner, ExitCodeGenerator {

    static final String USAGE = "Usage: create-shipment [--destination=<dst_id>]";

    private final ShipmentService shipmentService;
    private final PrintStream out;
    private final PrintStream err;
    private int exitCode;

    @Autowired
    public ShipmentCli(ShipmentService shipmentService) {
        this(shipmentService, System.out, System.err);
    }

    ShipmentCli(ShipmentService shipmentService, PrintStream out, PrintStream err) {
        this.shipmentService = shipmentService;
        this.out = out;
        this.err = err;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> command = args.getNonOptionArgs();
        List<String> destinations = args.getOptionValues("destination");
        boolean validShape = command.equals(List.of("create-shipment"))
                && args.getOptionNames().stream().allMatch("destination"::equals)
                && (destinations == null || destinations.size() == 1);
        if (!validShape) {
            fail(USAGE);
            return;
        }

        try {
            String destination = destinations != null ? destinations.get(0) : promptForDestination();
            Shipment shipment = shipmentService.create(destination);
            out.println("Created shipment " + shipment.getId() + " (status: " + shipment.getStatus()
                    + ") bound for " + shipment.getDestinationId());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }

    private String promptForDestination() {
        out.print("Destination id (e.g. dst_mars): ");
        out.flush();
        Scanner in = new Scanner(System.in);
        return in.hasNextLine() ? in.nextLine() : "";
    }

    private void fail(String message) {
        err.println(message);
        exitCode = 1;
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
