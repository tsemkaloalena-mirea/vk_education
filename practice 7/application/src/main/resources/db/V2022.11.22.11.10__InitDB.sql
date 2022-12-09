CREATE TABLE manufacturer (
name varchar(100) PRIMARY KEY
);

CREATE TABLE product (
id SERIAL PRIMARY KEY,
name varchar(100) NOT NULL,
amount integer NOT NULL,
manufacturer_name varchar(100) REFERENCES manufacturer(name) ON UPDATE CASCADE ON DELETE CASCADE
);