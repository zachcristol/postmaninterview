-- Minimal table proving the ORM reaches the database. The real Planet Express
-- schema arrives during the live session and lands in V2.
create table shipment (
    id bigint generated always as identity primary key,
    origin varchar(255) not null,
    destination varchar(255) not null,
    status varchar(255) not null,
    created_at timestamp not null
);
