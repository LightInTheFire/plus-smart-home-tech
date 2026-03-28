CREATE TABLE IF NOT EXISTS shopping_cart
(
    id         UUID PRIMARY KEY     DEFAULT uuidv7(),
    username   VARCHAR(64) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    is_active  BOOLEAN     NOT NULL default TRUE
);

CREATE TABLE IF NOT EXISTS shopping_cart_item
(
    id               UUID PRIMARY KEY DEFAULT uuidv7(),
    product_id       UUID,
    shopping_cart_id UUID   NOT NULL REFERENCES shopping_cart (id),
    quantity         BIGINT NOT NULL
);
