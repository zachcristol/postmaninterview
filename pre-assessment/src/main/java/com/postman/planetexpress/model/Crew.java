package com.postman.planetexpress.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA entity representing a crew member, mirroring the columns of data/crew.csv. */
@Entity
@Getter
public class Crew {

    @Id
    private String id;

    private String fullName;

    private String rank;

    private String species;

    private String homePlanetId;

    private String status;

    private Instant hiredAt;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> cargoCertifications;

    private int maxConsecutiveDays;

    private String pilotLicenseId;

    private String notes;

    protected Crew() {
    }

    public Crew(String id, String fullName, String rank, String species, String homePlanetId,
            Instant hiredAt, List<String> cargoCertifications, int maxConsecutiveDays) {
        this.id = id;
        this.fullName = fullName;
        this.rank = rank;
        this.species = species;
        this.homePlanetId = homePlanetId;
        this.status = "active";
        this.hiredAt = hiredAt;
        this.cargoCertifications = cargoCertifications;
        this.maxConsecutiveDays = maxConsecutiveDays;
    }
}
