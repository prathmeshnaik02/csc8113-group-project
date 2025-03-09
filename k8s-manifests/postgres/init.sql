CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    stock INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS cart (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    version INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

BEGIN;

INSERT INTO users (id, username, password, role) VALUES
(12345678, 'admin', '12345678', 'admin');

INSERT INTO book (id, title, price, stock, version) VALUES
(1, 'The Great Adventure', 19.99, 20, 1),
(2, 'Tech Innovations', 24.99, 0, 1),
(3, 'Mystery of the Missing Diamond', 14.99, 30, 1),
(4, 'The Lost World', 15.99, 53, 1),
(5, 'Beyond the Horizon', 22.50, 324, 1);

COMMIT;
