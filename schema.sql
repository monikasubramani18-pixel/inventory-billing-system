-- Inventory & Billing System: database schema (PostgreSQL)
-- Usage: create a database named billing_db, then run this file.

CREATE TABLE IF NOT EXISTS products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
  stock INT NOT NULL CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS customers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(15) UNIQUE
);

CREATE TABLE IF NOT EXISTS bills (
  id SERIAL PRIMARY KEY,
  customer_id INT REFERENCES customers(id),
  bill_date TIMESTAMP DEFAULT NOW(),
  total NUMERIC(10,2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS bill_items (
  id SERIAL PRIMARY KEY,
  bill_id INT REFERENCES bills(id) ON DELETE CASCADE,
  product_id INT REFERENCES products(id),
  quantity INT NOT NULL CHECK (quantity > 0),
  price NUMERIC(10,2) NOT NULL
);

-- Optional sample data (run once)
INSERT INTO products (name, price, stock) VALUES
  ('Pen', 10, 100),
  ('Notebook', 50, 40),
  ('Pencil Box', 120, 15);