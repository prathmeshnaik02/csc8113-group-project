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
  book_inventory (isbn, title, subtitle, author, published, publisher, pages, description, price, genre, stock_status, language, rating)
VALUES
  ('1', 'The Great Adventure', 'A Journey Through Time', 'John Doe', '2021', 'Adventure Press', 350, 'An epic story of adventure through historical events.', 19.99, 'Adventure', 'In Stock', 'English', 4.5),
  ('2', 'Tech Innovations', 'The Future of Technology', 'Alice Johnson', '2022', 'TechWorld Publications', 250, 'An exploration of groundbreaking technologies changing the world.', 24.99, 'Technology', 'Out of Stock', 'English', 4.2),
  ('3', 'Mystery of the Missing Diamond', NULL, 'Michael Lee', '2019', 'Mystery Books', 555, 'A thrilling mystery novel about a stolen diamond and the detective who seeks to recover it.', 14.99, 'Mystery', 'In Stock', 'English', 4.8),
  ('4', 'The Lost World', 'Discoveries Beneath the Surface', 'Sarah Johnson', '2020', 'Mystery Press', 285, 'A thrilling tale of uncharted territories and the mysteries they hide.', 15.99, 'Adventure', 'Out of Stock', 'English', 4.3),
  ('5', 'Beyond the Horizon', 'Exploring New Frontiers', 'Michael White', '2018', 'Explorer Books', 400, 'A gripping narrative of human exploration and discovery across the globe.', 22.50, 'Non-fiction', 'In Stock', 'English', 4.7),
  ('6', 'The Quantum Paradox', 'Understanding the Universe', 'Robert Chen', '2023', 'Science Publications', 320, 'A comprehensive guide to quantum physics and its philosophical implications.', 29.99, 'Science', 'In Stock', 'English', 4.6),
  ('7', 'Cooking Around the World', 'Global Cuisine Made Simple', 'Maria Garcia', '2022', 'Culinary Arts Press', 180, 'A collection of international recipes with step-by-step instructions for home cooks.', 18.50, 'Cooking', 'In Stock', 'English', 4.4),
  ('8', 'Financial Freedom', 'A Guide to Personal Investing', 'David Wilson', '2021', 'Finance House', 275, 'Strategic approaches to building wealth and securing financial independence.', 21.99, 'Finance', 'In Stock', 'English', 4.1),
  ('9', 'The Art of Mindfulness', 'Finding Peace in a Chaotic World', 'Emma Thompson', '2020', 'Wellness Publications', 210, 'Practical techniques for developing mindfulness and reducing stress in everyday life.', 16.99, 'Self-help', 'Out of Stock', 'English', 4.9),
  ('10', 'Historical Perspectives', 'Ancient Civilizations Uncovered', 'James Anderson', '2019', 'History Press', 450, 'An in-depth analysis of ancient civilizations and their lasting impact on modern society.', 26.50, 'History', 'In Stock', 'English', 4.3),
  ('11', 'The Digital Revolution', NULL, 'Thomas Brown', '2022', 'Tech Insights', 290, 'How digital technology is transforming industries and reshaping human behavior.', 23.75, 'Technology', 'In Stock', 'English', 4.5),
  ('12', 'Poetic Expressions', 'A Collection of Modern Verse', 'Olivia Martinez', '2021', 'Literary Works', 150, 'Contemporary poetry exploring themes of identity, nature, and human connection.', 12.99, 'Poetry', 'In Stock', 'English', 4.2),
  ('13', 'The Secret Garden', 'Finding Magic in Ordinary Places', 'Elizabeth Wright', '2020', 'Green Leaf Publishing', 240, 'A guide to creating beautiful and sustainable garden spaces in urban environments.', 19.50, 'Gardening', 'In Stock', 'English', 4.7),
  ('14', 'Medical Breakthroughs', 'The Future of Healthcare', 'Dr. Jonathan Lewis', '2023', 'Health Sciences', 380, 'Recent advances in medical research and their potential impact on human longevity.', 32.99, 'Medicine', 'Out of Stock', 'English', 4.8),
  ('15', 'The Cybersecurity Handbook', 'Protecting Digital Assets', 'Alexandra Kim', '2022', 'Digital Defense Press', 310, 'Essential strategies for individuals and organizations to safeguard against cyber threats.', 27.50, 'Technology', 'In Stock', 'English', 4.4);
