package com.postman.planetexpress.repository;

import com.postman.planetexpress.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository providing CRUD access to {@link Shipment} records. */
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
