create table if not exists usr.role_permission(
    role_id integer not null references user_db.usr.role(id) on delete cascade ,
    permission_id integer not null references user_db.usr.permission(id) on delete cascade,
    primary key (role_id, permission_id)
);