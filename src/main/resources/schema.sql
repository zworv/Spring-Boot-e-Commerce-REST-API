
DROP TABLE IF EXISTS users, products, carts, cart_items, orders, order_items;

CREATE TABLE IF NOT EXISTS users (
    id          INT     AUTO_INCREMENT  PRIMARY KEY,
    username    TEXT    NOT NULL,
    password    TEXT    NOT NULL,
    role        ENUM("ADMIN", "SELLER", "CUSTOMER") NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id          INT     AUTO_INCREMENT  PRIMARY KEY,
    seller_id   INT     NOT NULL,
    name        TEXT    NOT NULL,
    description TEXT    NOT NULL,
    quantity    INT     NOT NULL,
    price       FLOAT   NOT NULL,

    CONSTRAINT fk_products_users
        FOREIGN KEY (seller_id)
        REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS carts (
    id          INT     AUTO_INCREMENT  PRIMARY KEY,
    customer_id INT     NOT NULL,
    price       FLOAT   NOT NULL,

    CONSTRAINT fk_carts_users
        FOREIGN KEY (customer_id)
        REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id          INT     AUTO_INCREMENT  PRIMARY KEY,
    cart_id     INT     NOT NULL,
    product_id  INT     NOT NULL,
    name        TEXT    NOT NULL,
    quantity    INT     NOT NULL,
-- price is one item cost
    price       FLOAT   NOT NULL,

    CONSTRAINT fk_cart_items_carts
        FOREIGN KEY (cart_id)
        REFERENCES carts(id),

    CONSTRAINT fk_cart_items_products
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id                  INT     AUTO_INCREMENT  PRIMARY KEY,
    seller_id           INT     NOT NULL,
    customer_id         INT     NOT NULL,
    order_start_date    DATE,
    order_complete_date DATE,
    order_status        ENUM("PROCESSING", "CANCELED", "COMPLETED") NOT NULL,
    address             TEXT    NOT NULL,
    credit_card         TEXT    NOT NULL,
    price               FLOAT   NOT NULL,

    CONSTRAINT fk_orders_sellers
        FOREIGN KEY (seller_id)
        REFERENCES users(id),

    CONSTRAINT fk_orders_customers
        FOREIGN KEY (customer_id)
        REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_id            INT     NOT NULL,
    product_id          INT     NOT NULL,
    name                TEXT    NOT NULL,
    quantity            INT     NOT NULL,
-- price is one item cost
    price               FLOAT   NOT NULL,

    PRIMARY KEY (order_id, product_id),

    CONSTRAINT fk_order_items_orders
        FOREIGN KEY (order_id)
        REFERENCES orders(id),

    CONSTRAINT fk_order_items_products
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);