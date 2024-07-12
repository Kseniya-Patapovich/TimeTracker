create table if not exists public.users
(
    id       bigint       not null
    primary key,
    login    varchar(255),
    password varchar(255),
    role     varchar(255)
    constraint users_role_check
    check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[])),
    username varchar(255) not null
    );