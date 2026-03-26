CREATE TABLE IF NOT EXISTS addresses
(
    id      UUID PRIMARY KEY DEFAULT uuidv7(),
    country VARCHAR(64),
    city    VARCHAR(120),
    street  VARCHAR(120),
    house   VARCHAR(120),
    flat    VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS products
(
    id      UUID PRIMARY KEY DEFAULT uuidv7(),
    fragile BOOLEAN        NOT NULL,
    width   NUMERIC(10, 2) NOT NULL,
    height  NUMERIC(10, 2) NOT NULL,
    depth   NUMERIC(10, 2) NOT NULL,
    weight  NUMERIC(10, 2) NOT NULL,
    quantity BIGINT NOT NULL
)
