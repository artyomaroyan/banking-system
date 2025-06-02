create sequence if not exists account_number_seq
    start with 10000000000
    increment by 1
    no minvalue
    no maxvalue
    cache 1;