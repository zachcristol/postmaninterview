-- Schema modelled on data/shipments.csv, data/crew.csv and data/jobs.csv.
--
-- The placeholder V1 shipment table is replaced: the real ids are prefixed strings
-- (shp_7x9k2m), not generated numbers, and the row shape is entirely different.
--
-- The CSVs contain deliberate anomalies (statuses outside the documented enum, fractional
-- money, negative insurance, a missing destination, delivery before dispatch), so those
-- columns are typed and nullable to accept what the data actually holds and carry no CHECK
-- constraints. Validation belongs in the application layer.
--
-- Planets, destinations, routes, permits and packages have no CSV of their own, so
-- references to them are plain ids rather than foreign keys. crew.home_planet_id mixes
-- plnt_ and dst_ prefixes for the same reason.

drop table shipment;

create table shipment (
    id varchar(32) primary key,
    status varchar(32) not null,
    priority varchar(32) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    dispatched_at timestamptz,
    estimated_delivery timestamptz,
    origin_planet_id varchar(64) not null,
    origin_city varchar(255) not null,
    origin_continent varchar(255) not null,
    destination_id varchar(64),
    route_id varchar(64),
    permit_id varchar(64),
    customs_status varchar(32) not null,
    package_count integer not null check (package_count >= 0),
    declared_value_doop_dollars numeric(14, 2) not null,
    insurance_value_doop_dollars numeric(14, 2) not null,
    special_instructions text,
    etag varchar(64) not null
);

create table crew (
    id varchar(32) primary key,
    full_name varchar(255) not null,
    rank varchar(255) not null,
    species varchar(64) not null,
    home_planet_id varchar(64) not null,
    status varchar(32) not null,
    hired_at timestamptz not null,
    -- Semicolon-separated in the CSV; an empty array means no certifications on file.
    cargo_certifications text[] not null default '{}',
    max_consecutive_days integer not null,
    pilot_license_id varchar(64),
    notes text
);

create table job (
    id varchar(32) primary key,
    job_type varchar(64) not null,
    status varchar(32) not null,
    shipment_id varchar(32) not null references shipment (id),
    package_id varchar(32),
    created_at timestamptz not null,
    updated_at timestamptz not null,
    completed_at timestamptz,
    progress_current_step varchar(64),
    progress_steps_complete smallint,
    progress_steps_total smallint,
    progress_steps_json jsonb,
    result_json jsonb,
    error_code varchar(64),
    error_message text
);

create index job_shipment_id_idx on job (shipment_id);
