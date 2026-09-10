package com.postman.planetexpress;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final ShipmentRepository shipmentRepository;
    private final Object seedLock = new Object();

    public PingController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping("/api/ping")
    public Map<String, Object> ping() {
        long shipmentCount = seedIfEmpty();

        // LinkedHashMap, not Map.of: the README documents this exact response and
        // Map.of has no defined iteration order.
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("database", "sqlite");
        response.put("shipmentCount", shipmentCount);
        return response;
    }

    // Single-instance app, so a JVM-level lock around this check-then-act is enough:
    // it closes the race where concurrent requests all see count == 0 before any save
    // commits (20 parallel calls used to produce 10 shipments instead of 1).
    private long seedIfEmpty() {
        synchronized (seedLock) {
            if (shipmentRepository.count() == 0) {
                shipmentRepository.save(new Shipment("Earth", "Mars", "IN_TRANSIT"));
            }
            return shipmentRepository.count();
        }
    }
}
