-- DROP TABLE IF EXISTS "customer";
-- DROP TABLE IF EXISTS "account";
-- DROP TABLE IF EXISTS "transaction";
-- DROP TABLE IF EXISTS "loan";
-- DROP TABLE IF EXISTS "collateral";
-- DROP TABLE IF EXISTS "stock";
-- DROP TABLE IF EXISTS "holding";

-- Customer Table;
CREATE TABLE IF NOT EXISTS `customer` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `firstname`	TEXT NOT NULL,
    `lastname` TEXT NOT NULL,
    `email` TEXT NOT NULL UNIQUE,
    `date_of_birth`	TEXT NOT NULL,
    `age` INTEGER NOT NULL,
    `phone_number` INTEGER,
    `SSN` TEXT,
    `password` TEXT NOT NULL,
    `is_manager` INTEGER NOT NULL
);

-- INSERT ADMIN;
INSERT OR IGNORE INTO customer (id, firstname, lastname, email, date_of_birth, age, password, is_manager) values (1, 'admin', 'admin', 'admin@mail.com', '2000-01-01', 30, 'admin', true);

-- Account Table;
CREATE TABLE IF NOT EXISTS `account` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `account_no` INTEGER NOT NULL UNIQUE,
    `account_type` TEXT NOT NULL,
    `balance` REAL,
    FOREIGN KEY(cid) REFERENCES customer
);

-- Transaction Table;
CREATE TABLE IF NOT EXISTS `operations` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `aid` INTEGER NOT NULL,
    `message` TEXT NOT NULL,
    `today_date` TEXT NOT NULL,
    `old_value` REAL,
    `new_value` REAL,
    FOREIGN KEY(aid) REFERENCES account
);

-- Loan Table;
CREATE TABLE IF NOT EXISTS `loan` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `aid` INTEGER NOT NULL UNIQUE,
    `cid` INTEGER NOT NULL,
    `name` TEXT NOT NULL,
    `amount` REAL,
    `collateral_id` INTEGER NOT NULL,
    FOREIGN KEY(aid) REFERENCES account,
    FOREIGN KEY(cid) REFERENCES customer,
    FOREIGN KEY(collateral_id) REFERENCES collateral
);

-- Collateral Table;
CREATE TABLE IF NOT EXISTS `collateral` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `name` TEXT NOT NULL,
    `value` REAL,
    `in_use` INTEGER NOT NULL,
    FOREIGN KEY(cid) REFERENCES customer
);

-- Stock Table;
CREATE TABLE IF NOT EXISTS `stock` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `name` TEXT NOT NULL,
    `value` REAL NOT NULL
);

-- Holding Table;
CREATE TABLE IF NOT EXISTS `holding` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `sid` INTEGER NOT NULL,
    `quantity` INTEGER NOT NULL,
    `base_value` REAL NOT NULL,
    FOREIGN KEY(cid) REFERENCES customer,
    FOREIGN KEY(sid) REFERENCES stock
);
