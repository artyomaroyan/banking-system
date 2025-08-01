create table if not exists usr.role_permission(
    role_id uuid not null references user_db.usr.role(id) on delete cascade ,
    permission_id uuid not null references user_db.usr.permission(id) on delete cascade,
    primary key (role_id, permission_id)
);