create table if not exists transaction.outbox_event_entity(
    id uuid unique not null primary key default gen_random_uuid(),
    topic varchar(250) not null ,
    key varchar(300) not null ,
    aggregate_type varchar(250) not null ,
    aggregate_id varchar(250) not null ,
    type varchar(250) not null ,
    payload varchar(250) not null ,
    status varchar(20) not null ,
    tries integer not null ,
    last_error varchar(1000) not null ,
    created_at timestamp not null default CURRENT_TIMESTAMP
);