CREATE TABLE product (
    id bigserial NOT NULL,
    name varchar NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (id)
);

CREATE TABLE organisation (
    tin bigint NOT NULL,
    name varchar NOT NULL,
    account bigint NOT NULL,
    CONSTRAINT organisation_pk PRIMARY KEY (tin)
);

CREATE TABLE invoice (
    id bigserial NOT NULL,
    invoice_date date NOT NULL,
    organisation_tin bigint NOT NULL REFERENCES organisation(tin) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT invoice_pk PRIMARY KEY (id)
);

CREATE TABLE invoice_item (
    id bigserial NOT NULL,
    cost int NOT NULL,
    product_id bigint NOT NULL REFERENCES product(id) ON UPDATE CASCADE ON DELETE CASCADE,
    amount int NOT NULL,
    invoice_id bigint NOT NULL REFERENCES invoice(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT invoice_item_pk PRIMARY KEY (id)
);
