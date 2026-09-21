package com.postman.planetexpress.service;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.repository.ShipmentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final Object seedLock = new Object();

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    // Single-instance app, so a JVM-level lock around this check-then-act is enough:
    // it closes the race where concurrent requests all see count == 0 before any save
    // commits (20 parallel calls used to produce 10 shipments instead of 1).
    public long seedIfEmpty() {
        synchronized (seedLock) {
            if (shipmentRepository.count() == 0) {
                shipmentRepository.save(new Shipment("shp_seed01", "dst_mars"));
            }
            return shipmentRepository.count();
        }
    }

    /** Creates a draft shipment out of New New York bound for the given destination. */
    public Shipment createShipment(String destinationId) {
        // Same shape as the CSV ids: "shp_" plus six lowercase alphanumerics.
        String id = "shp_" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return shipmentRepository.save(new Shipment(id, destinationId));
    }

    public List<Shipment> listShipments() {
        return shipmentRepository.findAll();
    }
}
