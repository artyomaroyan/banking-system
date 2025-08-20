create table if not exists account.account(
    id uuid unique not null primary key default gen_random_uuid(),
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP,
    account_owner_id uuid not null ,
    account_number varchar(16) unique not null ,
    account_owner_username varchar(30) not null ,
    balance bigint not null ,
    account_type varchar(30) not null,
    currency varchar(10) not null
);