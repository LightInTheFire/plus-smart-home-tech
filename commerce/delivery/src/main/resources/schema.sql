CREATE TABLE IF NOT EXISTS addresses
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(64),
    city    VARCHAR(120),
    street  VARCHAR(120),
    house   VARCHAR(120),
    flat    VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS deliveries
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id        UUID        NOT NULL,
    from_address_id UUID        NOT NULL,
    to_address_id   UUID        NOT NULL,
    state           VARCHAR(20) NOT NULL,
    CONSTRAINT fk_deliveries_from_address
        FOREIGN KEY (from_address_id)
            REFERENCES addresses (id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_deliveries_to_address
        FOREIGN KEY (to_address_id)
            REFERENCES addresses (id)
            ON DELETE RESTRICT
);
