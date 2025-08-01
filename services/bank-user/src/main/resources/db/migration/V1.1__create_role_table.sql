create extension if not exists pgcrypto;

create table if not exists usr.role(
    id uuid unique not null primary key default gen_random_uuid() ,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP,
    role_name varchar(30) not null unique
);