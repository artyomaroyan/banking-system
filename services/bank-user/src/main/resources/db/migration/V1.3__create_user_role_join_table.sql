create table if not exists usr.user_role(
    id uuid not null unique default gen_random_uuid(),
    user_id uuid not null references user_db.usr.usr(id) on delete cascade ,
    role_id uuid not null references user_db.usr.role(id) on delete cascade ,
    primary key (id, user_id, role_id)
);