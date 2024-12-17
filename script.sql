CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE
);

CREATE TABLE token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255),
    pin VARCHAR(6), -- PIN à 6 chiffres
    expiration TIMESTAMP NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
