create extension if not exists pgcrypto;

create table if not exists usr.user_role(
    id uuid unique not null primary key default gen_random_uuid() ,
    user_id uuid not null references user_db.usr.usr(id) on delete cascade ,
    role_id uuid not null references user_db.usr.role(id) on delete cascade
);