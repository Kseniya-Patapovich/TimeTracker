create table if not exists project
(
    id      bigint       not null
    primary key,
    project varchar(255) not null
    );

create table if not exists public.users
(
    id       bigint       not null
    primary key,
    username varchar(255) not null
    );

create table if not exists public.record
(
    id         bigint not null
    primary key,
    project_id bigint not null
    constraint fk1y6vpd46d3j4rxf24q7ut2x7i
    references public.project,
    start_time timestamp(6),
    end_time   timestamp(6),
    users_id   bigint not null
    constraint fkrjgeoedr78c4n59t2oxayqhw0
    references public.users
    );