create table if not exists user_db.usr.user_role(
    user_id integer not null ,
    role_id integer not null ,
    primary key (user_id, role_id),
    foreign key (user_id) references user_db.usr.usr(id) on delete cascade ,
    foreign key (role_id) references user_db.usr.role(id) on delete cascade
);