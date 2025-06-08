create table if not exists user_db.usr.role_permission(
    role_id integer not null ,
    permission_id integer not null ,
    primary key (role_id, permission_id),
    foreign key (role_id) references user_db.usr.role(id) on delete cascade ,
    foreign key (permission_id) references user_db.usr.permission(id) on delete cascade
);