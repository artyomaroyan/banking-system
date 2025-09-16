create table if not exists transaction.account_balance_projection(
    account_id uuid not null primary key ,
    balance bigint not null ,
    version integer not null ,
    updated_at timestamp not null default CURRENT_TIMESTAMP
);