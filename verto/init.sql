-- Initialize the database
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Create the products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    stock_quantity INT NOT NULL DEFAULT 0,
    low_stock_threshold INT NOT NULL DEFAULT 5,
    CONSTRAINT chk_stock_quantity CHECK (stock_quantity >= 0),
    CONSTRAINT chk_low_stock_threshold CHECK (low_stock_threshold >= 0)
);
