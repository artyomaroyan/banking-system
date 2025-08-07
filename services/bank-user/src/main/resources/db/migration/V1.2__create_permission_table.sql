create table if not exists usr.permission(
    id uuid unique not null primary key default gen_random_uuid(),
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP,
    permission_name varchar(50) not null unique
);