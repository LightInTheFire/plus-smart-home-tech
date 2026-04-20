CREATE TABLE IF NOT EXISTS payments
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id       UUID           NOT NULL,
    total_payment  NUMERIC(10, 2) NOT NULL,
    delivery_total NUMERIC(10, 2) NOT NULL,
    fee_total      NUMERIC(10, 2) NOT NULL,
    status         VARCHAR(15)    NOT NULL CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED'))
);
