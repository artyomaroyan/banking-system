create table if not exists notification.notification(
    id uuid unique not null primary key default gen_random_uuid(),
    created_at timestamp not null default current_timestamp ,
    updated_at timestamp not null default current_timestamp ,
    email_type varchar(20) not null ,
    notification_date timestamp not null ,
    recipient_email varchar(100),
    username varchar(30),
    verification_link varchar(800)
);