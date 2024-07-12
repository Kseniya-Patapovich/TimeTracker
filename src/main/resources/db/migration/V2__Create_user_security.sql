create table if not exists public.user_security
(
    id            bigint not null
        primary key,
    user_id       bigint,
    role          varchar(255)
        constraint user_security_role_check
            check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[])),
    user_login    varchar(255),
    user_password varchar(255)
);