create extension if not exists pgcrypto;

create table if not exists usr.role_permission(
    id uuid unique not null primary key default gen_random_uuid() ,
    role_id uuid not null references user_db.usr.role(id) on delete cascade ,
    permission_id uuid not null references user_db.usr.permission(id) on delete cascade
);