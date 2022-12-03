--DROP SCHEMA IF EXISTS security CASCADE;
CREATE SCHEMA IF NOT EXISTS security;

CREATE TABLE IF NOT EXISTS security.users
(
    id SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE,
    password varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS security.roles
(
    id SERIAL PRIMARY KEY,
    role varchar(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS security.user_roles
(
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO security.roles (id, role) VALUES
(1, 'MANAGER'),
(2, 'GUEST');

INSERT INTO security.users (id, name, password) VALUES
(1, 'manager1', 'pswd'),
(2, 'usr1', 'usr');

INSERT INTO security.user_roles VALUES
(1, 1),
(1, 2),
(2, 2);