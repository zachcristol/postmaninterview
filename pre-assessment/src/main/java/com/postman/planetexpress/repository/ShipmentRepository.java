package com.postman.planetexpress.repository;

import com.postman.planetexpress.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
