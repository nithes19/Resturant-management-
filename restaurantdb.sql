
-- Create database
CREATE DATABASE IF NOT EXISTS restaurantdb;

-- Use the database
USE restaurantdb;

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    orderid INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    food_item VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    table_number VARCHAR(10) NOT NULL,
    contact VARCHAR(20) NOT NULL
);
