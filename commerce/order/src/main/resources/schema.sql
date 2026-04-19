CREATE TABLE IF NOT EXISTS orders
(
    id               UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    username         VARCHAR(120) NOT NULL,
    shopping_cart_id UUID,
    payment_id       UUID,
    delivery_id      UUID,
    state            VARCHAR(50)  NOT NULL DEFAULT 'NEW',
    delivery_weight  DOUBLE PRECISION,
    delivery_volume  DOUBLE PRECISION,
    fragile          BOOLEAN,
    total_price      NUMERIC(10, 2),
    delivery_price   NUMERIC(10, 2),
    product_price    NUMERIC(10, 2),
    created_at       TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id   UUID   NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id UUID   NOT NULL,
    quantity   BIGINT NOT NULL CHECK (quantity > 0),
    UNIQUE (order_id, product_id)
);

CREATE TABLE IF NOT EXISTS order_address
(
    order_id UUID PRIMARY KEY NOT NULL,
    country  VARCHAR(64),
    city     VARCHAR(120),
    street   VARCHAR(120),
    house    VARCHAR(120),
    flat     VARCHAR(120)
)
