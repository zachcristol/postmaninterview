package com.postman.planetexpress.controller;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.service.ShipmentService;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Exposes shipment creation and listing. */
@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    /** Request body for creating a shipment; everything but the destination is defaulted. */
    public record CreateShipmentRequest(String destinationId) {
    }

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<Shipment> list() {
        return shipmentService.listShipments();
    }

    @PostMapping
    public ResponseEntity<Shipment> create(@RequestBody CreateShipmentRequest request) {
        Shipment shipment;
        try {
            shipment = shipmentService.create(request.destinationId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.created(URI.create("/api/shipments/" + shipment.getId())).body(shipment);
    }
}
