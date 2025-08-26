create table if not exists account.outbox_event(
    id uuid unique not null primary key default gen_random_uuid() ,
    aggregate_id uuid not null ,
    aggregate_type varchar(50) not null ,
    type varchar(50) not null ,
    payload jsonb not null ,
    occurred_at timestamptz not null ,
    published boolean not null default false
);

create index on account.outbox_event (published);