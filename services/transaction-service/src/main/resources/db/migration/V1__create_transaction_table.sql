create table if not exists transaction.transaction (
    id uuid unique not null primary key default gen_random_uuid(),
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP,
    userId uuid not null ,
    debit_account varchar(16) not null ,
    credit_account varchar(16) not null ,
    amount bigint not null ,
    status varchar(15) not null ,
    reservation_id varchar(50) not null ,
    idempotency_key varchar(50) not null ,
    type varchar(15) not null
)