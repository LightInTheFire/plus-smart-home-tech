CREATE TABLE IF NOT EXISTS addresses
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(64),
    city    VARCHAR(120),
    street  VARCHAR(120),
    house   VARCHAR(120),
    flat    VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS products
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fragile  BOOLEAN        NOT NULL,
    width    NUMERIC(10, 2) NOT NULL,
    height   NUMERIC(10, 2) NOT NULL,
    depth    NUMERIC(10, 2) NOT NULL,
    weight   NUMERIC(10, 2) NOT NULL,
    quantity BIGINT         NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id         UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL default now()
);

CREATE TABLE IF NOT EXISTS order_bookings
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    UUID   NOT NULL REFERENCES orders (id),
    product_id  UUID   NOT NULL REFERENCES products (id),
    delivery_id UUID   NOT NULL,
    quantity    BIGINT not null
);
