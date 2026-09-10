package com.postman.planetexpress;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final ShipmentRepository shipmentRepository;

    public PingController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping("/api/ping")
    public Map<String, Object> ping() {
        if (shipmentRepository.count() == 0) {
            shipmentRepository.save(new Shipment("Earth", "Mars", "IN_TRANSIT"));
        }
        long shipmentCount = shipmentRepository.count();

        // LinkedHashMap, not Map.of: the README documents this exact response and
        // Map.of has no defined iteration order.
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("database", "sqlite");
        response.put("shipmentCount", shipmentCount);
        return response;
    }
}
