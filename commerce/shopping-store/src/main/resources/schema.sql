CREATE TABLE IF NOT EXISTS products
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(128)     NOT NULL,
    description VARCHAR(512)     NOT NULL,
    image_src   VARCHAR(512)     NOT NULL,
    quantity    VARCHAR(16)      NOT NULL CHECK (quantity IN ('ENDED', 'FEW', 'ENOUGH', 'MANY')),
    state       VARCHAR(16)      NOT NULL CHECK (state IN ('ACTIVE', 'DEACTIVATE')),
    category    VARCHAR(16)      NOT NULL CHECK (category IN ('LIGHTING', 'CONTROL', 'SENSORS')),
    price       NUMERIC(10, 2)
);
