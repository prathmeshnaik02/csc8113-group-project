CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS book_inventory (
    isbn VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    author VARCHAR(255) NOT NULL,
    published VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    pages INTEGER NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    genre VARCHAR(255) NOT NULL,
    stock_status VARCHAR(255) NOT NULL,
    language VARCHAR(255) NOT NULL,
    rating DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGSERIAL PRIMARY KEY,
    book_isbn VARCHAR(255),
    quantity INTEGER NOT NULL,
    user_id VARCHAR(255)
);

INSERT INTO
  users (id, username, password, role)
VALUES
  (12345678, 'admin', '12345678', 'admin');

INSERT INTO
  book_inventory (isbn, title, subtitle, author, published, publisher, pages, description, price, genre, stock_status, language, rating)
VALUES
  ('1', 'The Great Adventure', 'A Journey Through Time', 'John Doe', '2021', 'Adventure Press', 350, 'An epic story of adventure through historical events.', 19.99, 'Adventure', 'In Stock', 'English', 4.5),
  ('2', 'Tech Innovations', 'The Future of Technology', 'Alice Johnson', '2022', 'TechWorld Publications', 250, 'An exploration of groundbreaking technologies changing the world.', 24.99, 'Technology', 'Out of Stock', 'English', 4.2),
  ('3', 'Mystery of the Missing Diamond', NULL, 'Michael Lee', '2019', 'Mystery Books', 555, 'A thrilling mystery novel about a stolen diamond and the detective who seeks to recover it.', 14.99, 'Mystery', 'In Stock', 'English', 4.8),
  ('4', 'The Lost World', 'Discoveries Beneath the Surface', 'Sarah Johnson', '2020', 'Mystery Press', 285, 'A thrilling tale of uncharted territories and the mysteries they hide.', 15.99, 'Adventure', 'Out of Stock', 'English', 4.3),
  ('5', 'Beyond the Horizon', 'Exploring New Frontiers', 'Michael White', '2018', 'Explorer Books', 400, 'A gripping narrative of human exploration and discovery across the globe.', 22.50, 'Non-fiction', 'In Stock', 'English', 4.7);
