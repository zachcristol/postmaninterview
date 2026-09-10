# Planet Express - Pre-Assessment

Spring Boot service backed by SQLite, proving the DB and backend are wired together.

## Stack

- Java 21 / Spring Boot 3.3
- Spring Data JPA + Hibernate (via `hibernate-community-dialects` for SQLite support)
- SQLite (`org.xerial:sqlite-jdbc`)

## Run it

```
docker compose up --build
```

This builds the Spring Boot app (multi-stage Maven build) and starts it on port 8080,
with the SQLite file at `/data/planetexpress.db` inside the container.

## Verify the connection

Health check (Spring Actuator's DB health indicator - reports `UP` only if it can open
a connection to the SQLite file):

```
curl localhost:8080/actuator/health
```

End-to-end proof via the ORM (seeds one row on first call, then counts rows through
the `ShipmentRepository`):

```
curl localhost:8080/api/ping
```

Expected response:

```json
{"status":"ok","database":"sqlite","shipmentCount":1}
```

## Scope

Per the pre-assessment guide, this intentionally stops at proving connectivity: one
minimal entity (`Shipment`), no UI/CLI/SDK, and no real schema or business logic since
that depends on the actual Planet Express data provided during the live session.
