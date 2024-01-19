DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    firstname VARCHAR(50)  NOT NULL,
    username  VARCHAR(50)  NOT NULL,
    password  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    address   VARCHAR(100),
    role      VARCHAR(50)  NOT NULL
);
