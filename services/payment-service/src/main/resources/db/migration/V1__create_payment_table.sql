create table if not exists payment.payment (
    id uuid unique not null primary key default gen_random_uuid(),
    userId uuid not null ,
    username varchar(30) not null ,
    amount bigint not null ,
    credit_account varchar(16) not null ,
    debit_account varchar(16) not null ,
    currency varchar(10) not null ,
    description varchar(100) not null ,
    payment_type varchar(25) not null
)