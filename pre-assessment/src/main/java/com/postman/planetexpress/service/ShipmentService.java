package com.postman.planetexpress.service;

import com.postman.planetexpress.model.Shipment;
import com.postman.planetexpress.repository.ShipmentRepository;
import java.security.SecureRandom;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    private static final Pattern DESTINATION_ID = Pattern.compile("dst_[a-z0-9_]{1,59}");
    private static final String ID_ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_SUFFIX_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

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

    /**
     * Creates and persists a draft shipment bound for the given destination.
     *
     * @throws IllegalArgumentException if the destination is not a {@code dst_} id
     */
    public Shipment create(String destinationId) {
        String destination = destinationId == null ? "" : destinationId.trim();
        if (!DESTINATION_ID.matcher(destination).matches()) {
            throw new IllegalArgumentException(
                    "Invalid destination '" + destination + "': expected an id like dst_mars");
        }
        return shipmentRepository.save(new Shipment(newId(), destination));
    }

    private String newId() {
        String id;
        do {
            StringBuilder suffix = new StringBuilder(ID_SUFFIX_LENGTH);
            for (int i = 0; i < ID_SUFFIX_LENGTH; i++) {
                suffix.append(ID_ALPHABET.charAt(RANDOM.nextInt(ID_ALPHABET.length())));
            }
            id = "shp_" + suffix;
        } while (shipmentRepository.existsById(id));
        return id;
    }
}
