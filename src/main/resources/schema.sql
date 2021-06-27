CREATE TABLE IF NOT EXISTS users
(
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(10) NOT NULL,
    email VARCHAR(20) NOT NULL,
    level TINYINT NOT NULL,
    login INT NOT NULL,
    recommend INT NOT NULL
);