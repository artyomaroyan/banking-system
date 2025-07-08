create table if not exists usr.user_role(
    user_id integer not null references user_db.usr.usr(id) on delete cascade ,
    role_id integer not null references user_db.usr.role(id) on delete cascade,
    primary key (user_id, role_id)
);