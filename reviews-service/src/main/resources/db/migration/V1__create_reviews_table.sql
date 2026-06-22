CREATE TABLE reviews (
                         id            BIGSERIAL PRIMARY KEY,
                         attraction_id BIGINT NOT NULL,
                         author        VARCHAR(100) NOT NULL,
                         rating        SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
                         text          VARCHAR(2000),
                         created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);