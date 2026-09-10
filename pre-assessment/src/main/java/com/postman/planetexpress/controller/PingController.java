package com.postman.planetexpress.controller;

import com.postman.planetexpress.service.ShipmentService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final ShipmentService shipmentService;

    public PingController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/api/ping")
    public Map<String, Object> ping() {
        long shipmentCount = shipmentService.seedIfEmpty();

        // LinkedHashMap, not Map.of: the README documents this exact response and
        // Map.of has no defined iteration order.
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("database", "postgres");
        response.put("shipmentCount", shipmentCount);
        return response;
    }
}
