package com.postman.planetexpress.repository;

import com.postman.planetexpress.model.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository providing CRUD access to {@link Crew} records. */
public interface CrewRepository extends JpaRepository<Crew, String> {
}
