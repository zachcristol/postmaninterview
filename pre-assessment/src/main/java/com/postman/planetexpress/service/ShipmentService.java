package com.postman.planetexpress.service;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.repository.ShipmentRepository;
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
}
