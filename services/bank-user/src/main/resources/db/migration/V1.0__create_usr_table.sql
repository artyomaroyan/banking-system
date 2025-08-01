create extension if not exists pgcrypto;

create table if not exists usr.usr(
    id uuid unique not null primary key default gen_random_uuid(),
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP,
    username varchar(30) not null unique ,
    first_name varchar(50) not null ,
    last_name varchar(50) not null ,
    email varchar(70) not null unique ,
    password varchar(800) not null ,
    phone varchar(15) not null unique ,
    age integer not null ,
    account_state varchar(20) not null
);