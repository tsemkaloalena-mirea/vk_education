CREATE TABLE roles (
id SERIAL NOT NULL,
name varchar(64) NOT NULL
);

CREATE TABLE permissions (
id SERIAL NOT NULL,
name varchar(64) NOT NULL,
role_id bigint REFERENCES roles(id)
);

ALTER TABLE users ADD COLUMN role_id bigint REFERENCES roles(id);