create user app
    superuser
    createdb
    createrole
    replication
    bypassrls;

ALTER USER app WITH PASSWORD 'app';

create table app_user
(
    id                         bigint       not null
        primary key,
    cash_account               numeric(19, 2),
    email                      varchar(255) not null
        constraint unique_email_constraint unique,
    is_account_non_expired     boolean      not null,
    is_account_non_locked      boolean      not null,
    is_credentials_non_expired boolean      not null,
    is_enabled                 boolean      not null,
    name                       varchar(255),
    password                   varchar(255),
    phone_number               varchar(255),
    user_role                  varchar(255)
);

alter table app_user
    owner to app;

create table car
(
    id                bigint not null
        primary key,
    car_number        varchar(255),
    car_state         varchar(255),
    latitude          double precision,
    longitude         double precision,
    name              varchar(255),
    price_per_minute  numeric(19, 2),
    rented_from       timestamp,
    current_rented_id bigint
        constraint current_renter_foreign_key
            references app_user,
    owner_id          bigint not null
        constraint car_owner_foreign_key
            references app_user
);

alter table car
    owner to app;

create table confirmation_token
(
    id           bigint not null
        primary key,
    confirmed_at timestamp,
    created_at   timestamp    not null,
    expires_at   timestamp    not null,
    token        varchar(255) not null,
    app_user_id  bigint       not null
        constraint user_token_foreign_key
            references app_user
);

alter table confirmation_token
    owner to app;

create sequence car_sequence_id;

alter sequence car_sequence_id owner to app;

create sequence confirmation_token_sequence_id;

alter sequence confirmation_token_sequence_id owner to app;

create sequence user_sequence_id;

alter sequence user_sequence_id owner to app;
