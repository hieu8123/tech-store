-- Disable foreign key checks to avoid constraint issues
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables if they exist before recreating them
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS banners;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS brands;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
                                     id BINARY(16) PRIMARY KEY,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     username VARCHAR(255),
                                     password VARCHAR(255) NOT NULL,
                                     phone_number VARCHAR(20) UNIQUE,
                                     avatar TEXT,
                                     role VARCHAR(50) NOT NULL DEFAULT 'USER',
                                     refresh_token_id BINARY(16) UNIQUE,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
                                              id BINARY(16) PRIMARY KEY,
                                              user_id BINARY(16) UNIQUE NOT NULL,
                                              token VARCHAR(512) UNIQUE NOT NULL,
                                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS addresses (
                                         id BINARY(16) PRIMARY KEY,
                                         user_id BINARY(16) NOT NULL,
                                         address TEXT NOT NULL,
                                         city VARCHAR(100) NOT NULL,
                                         postal_code VARCHAR(20) NOT NULL,
                                         is_primary BOOLEAN NOT NULL DEFAULT FALSE,
                                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS brands (
                                      id BINARY(16) PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      image TEXT,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
                                          id BINARY(16) PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
                                          image TEXT,
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
                                        id BINARY(16) PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        price INT NOT NULL,
                                        old_price INT,
                                        image TEXT,
                                        description TEXT,
                                        specification TEXT,
                                        buy_turn INT DEFAULT 0,
                                        quantity INT NOT NULL,
                                        brand_id BINARY(16) NOT NULL,
                                        category_id BINARY(16) NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS banners (
                                       id BINARY(16) PRIMARY KEY,
                                       image TEXT,
                                       name VARCHAR(255),
                                       status VARCHAR(50) NOT NULL,
                                       product_id BINARY(16) UNIQUE,
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
                                      id BINARY(16) PRIMARY KEY,
                                      user_id BINARY(16) NOT NULL,
                                      status VARCHAR(50) NOT NULL,
                                      note TEXT,
                                      total INT NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_details (
                                             id BINARY(16) PRIMARY KEY,
                                             order_id BINARY(16) NOT NULL,
                                             product_id BINARY(16) NOT NULL,
                                             price INT NOT NULL,
                                             quantity INT NOT NULL,
                                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add foreign key constraints after table creation
ALTER TABLE users ADD CONSTRAINT fk_users_refresh_token FOREIGN KEY (refresh_token_id) REFERENCES refresh_tokens(id);
ALTER TABLE refresh_tokens ADD CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE addresses ADD CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE products ADD CONSTRAINT fk_products_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE;
ALTER TABLE products ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE;
ALTER TABLE banners ADD CONSTRAINT fk_banners_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL;
ALTER TABLE orders ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE order_details ADD CONSTRAINT fk_order_details_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;
ALTER TABLE order_details ADD CONSTRAINT fk_order_details_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;
