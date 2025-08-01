create extension if not exists pgcrypto;

create table if not exists security.user_token(
    id uuid unique not null primary key default gen_random_uuid() ,
    user_id integer not null ,
    created_at timestamp not null default current_timestamp ,
    updated_at timestamp not null default current_timestamp,
    expires_at timestamp not null ,
    token varchar(2048) not null ,
    token_state varchar(30) not null ,
    token_purpose varchar(30) not null
);

create or replace function prevent_id_update() returns trigger as $$
    begin
        if new.id <> old.id then
            raise exception 'ID is not updatable';
        end if;
        return new;
end;
$$ language plpgsql;
end;

create trigger trg_prevent_id_update
    before update on security.user_token
    for each row
    execute function prevent_id_update();