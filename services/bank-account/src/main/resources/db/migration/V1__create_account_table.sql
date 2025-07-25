create table if not exists account.account(
    id integer unique not null generated by default as identity primary key ,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP,
    account_owner_id integer unique not null ,
    account_number varchar(16) unique not null ,
    account_owner_username varchar(30) not null ,
    balance bigint not null ,
    account_type varchar(30) not null,
    account_currency varchar(10) not null
);