CREATE TABLE roles (
id SERIAL NOT NULL,
name varchar(64) NOT NULL,
CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE permissions (
id SERIAL NOT NULL,
name varchar(64) NOT NULL,
role_id SERIAL REFERENCES roles(id),
CONSTRAINT permissions_pk PRIMARY KEY (id)
);

ALTER TABLE users ADD COLUMN role_id SERIAL REFERENCES roles(id);