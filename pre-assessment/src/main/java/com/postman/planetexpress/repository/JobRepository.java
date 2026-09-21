package com.postman.planetexpress.repository;

import com.postman.planetexpress.model.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository providing CRUD access to {@link Job} records. */
public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByShipmentId(String shipmentId);
}
