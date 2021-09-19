create table if not exists users
(
    id            uuid          primary key,
    login         varchar(64)   not null,

    first_name    varchar(64)   not null,
    last_name     varchar(64)   not null,

    birthday      date          not null,

    password      varchar(60)   not null,

    about_me      varchar(4096) not null,
    address       varchar(1024) not null,

    role          varchar(64)   not null
);