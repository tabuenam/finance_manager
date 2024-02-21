DROP TABLE IF EXISTS refresh_tokens;

CREATE TABLE refresh_tokens(
    id SERIAL PRIMARY KEY,
    refresh_token VARCHAR(20000) NOT NULL UNIQUE,
    revoked BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);