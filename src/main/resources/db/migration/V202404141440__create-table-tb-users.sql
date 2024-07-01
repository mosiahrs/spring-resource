CREATE TABLE IF NOT EXISTS tb_users (
    id_user serial primary key,
    username varchar (50) not null unique,
    password varchar (255) not null
);