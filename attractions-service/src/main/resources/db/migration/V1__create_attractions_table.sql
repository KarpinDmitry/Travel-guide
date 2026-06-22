CREATE TABLE attractions (
                             id          BIGSERIAL PRIMARY KEY,
                             name        VARCHAR(255) NOT NULL,
                             category    VARCHAR(100) NOT NULL,
                             city        VARCHAR(100) NOT NULL,
                             latitude    DOUBLE PRECISION NOT NULL,
                             longitude   DOUBLE PRECISION NOT NULL,
                             description TEXT,
                             created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);