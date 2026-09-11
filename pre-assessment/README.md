# Planet Express - Pre-Assessment

Spring Boot service backed by Postgres, proving the DB and backend are wired together.

## Stack

- Java 21 / Spring Boot 3.3
- Spring Data JPA + Hibernate
- Flyway for schema migrations
- Postgres 16 (`org.postgresql:postgresql`)

## Run it

```
docker compose up --build
```

This starts a Postgres container plus the Spring Boot app (multi-stage Maven build) on
port 8080. The backend waits for Postgres's healthcheck before starting.

Credentials come from `.env` (gitignored), with the same throwaway defaults baked into
`docker-compose.yml` so it runs standalone if `.env` is missing. Copy `.env.example` to
`.env` if you want to override them.

## Verify the connection

Health check (Spring Actuator's DB health indicator - reports `UP` only if it can open
a connection to Postgres):

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
{"status":"ok","database":"postgres","shipmentCount":1}
```

## Schema changes

Flyway owns the schema. Migrations live in `src/main/resources/db/migration` and run at
startup, before Hibernate opens a session; `ddl-auto` is `validate`, so Hibernate checks
the entities against the migrated schema and refuses to start on a mismatch rather than
altering tables behind your back.

To change the schema, add the next numbered file (`V2__...sql`) and restart. Flyway
applies only what hasn't run yet, so existing data is preserved. Never edit an applied
migration - Flyway checksums them and will fail the next startup.

To start over from an empty database:

```
docker compose down -v && docker compose up --build
```

## Tests

Two layers, matching the two things that can break: the JVM code, and the containerized
deliverable itself. Both run against a real Postgres container via Testcontainers (never
the dev volume), not H2 - several behaviors here (like `Shipment`'s identity-column id
mapping) are Postgres-specific and would pass against H2 even if broken.

**JVM tests** (`ShipmentRepositoryTest`, `PingControllerTest`) - repository round-trips and
the `/actuator/health` + `/api/ping` contract, each against a fresh Testcontainers Postgres
instance with a real Flyway run. Requires Docker to be running:

```
mvn test
```

Local Maven must run on JDK 21, not just `>=21` - if your machine has a newer JDK on
`JAVA_HOME` (e.g. via Homebrew), Lombok's annotation processor (used for entity getters)
silently no-ops instead of failing loudly, so `Shipment`'s getters won't exist and the
build fails with confusing "cannot find symbol" errors. Point `JAVA_HOME` at a JDK 21
install before running Maven locally:

```
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

Or, without local Maven, through the same image the `Dockerfile` builds with:

```
docker run --rm -v "$PWD":/build -w /build maven:3.9-eclipse-temurin-21 mvn test
```

**Container smoke test** - automates the "Verify the connection" steps above against the
actual `docker compose` stack:

```
./scripts/smoke-test.sh
```

## Scope

Per the pre-assessment guide, this intentionally stops at proving connectivity: one
minimal entity (`Shipment`), no UI/CLI/SDK, and no real schema or business logic since
that depends on the actual Planet Express data provided during the live session.
