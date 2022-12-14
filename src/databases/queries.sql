DROP TABLE IF EXISTS "customer";
DROP TABLE IF EXISTS "account";
DROP TABLE IF EXISTS "transaction";
DROP TABLE IF EXISTS "loan";
DROP TABLE IF EXISTS "collateral";
DROP TABLE IF EXISTS "stock";
DROP TABLE IF EXISTS "holding";

-- Customer Table
-- TODO (shubham) should be masked when storing on database
CREATE TABLE `customer` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `firstname`	TEXT NOT NULL,
    `lastname` TEXT NOT NULL,
    `email` TEXT NOT NULL UNIQUE,
    `date_of_birth`	TEXT NOT NULL,
    `age` INTEGER NOT NULL,
    `phone_number` INTEGER,
    `SSN` TEXT,
    `password`	TEXT NOT NULL,
    `is_manager` INTEGER NOT NULL
);

-- INSERT ADMIN
insert into customer (id, firstname, lastname, email, date_of_birth, age, password, is_manager) values (1, 'admin', 'admin', 'admin@mail.com', '01/01/2000', 30, 'admin', true);

-- Account Table
-- TODO (shubham) put a check constraint for 'saving', 'checking'
-- TODO (shubham) put a check constraint for MIN balance for account
CREATE TABLE `account` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `account_no` INTEGER NOT NULL UNIQUE,
    `account_type` TEXT NOT NULL,
    `balance` REAL,
    FOREIGN KEY(cid) REFERENCES customer
);

-- Transaction Table
CREATE TABLE `transaction` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `aid` INTEGER NOT NULL,
    `message` TEXT NOT NULL,
    `date` TEXT NOT NULL,
    `old_value` REAL,
    `new_value` REAL,
    FOREIGN KEY(aid) REFERENCES account
);

-- Loan Table
CREATE TABLE `loan` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `aid` INTEGER NOT NULL UNIQUE,
    `cid` INTEGER NOT NULL,
    `name` TEXT NOT NULL,
    `amount` REAL,
    `collateral_id` INTEGER NOT NULL,
    FOREIGN KEY(aid) REFERENCES account, FOREIGN KEY(cid) REFERENCES customer,
    FOREIGN KEY(collateral_id) REFERENCES collateral
);

-- Collateral Table
CREATE TABLE `collateral` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `name` TEXT NOT NULL,
    `value` REAL,
    `in_use` INTEGER NOT NULL,
    FOREIGN KEY(cid) REFERENCES customer
);

-- Stock Table
CREATE TABLE `stock` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `name` TEXT NOT NULL,
    `value` REAL NOT NULL
);

-- Holding Table
-- TODO add check constraint on quantity and base_value
CREATE TABLE `holding` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `sid` INTEGER NOT NULL,
    `quantity` INTEGER NOT NULL,
    `base_value` REAL NOT NULL,
    FOREIGN KEY(cid) REFERENCES customer,
    FOREIGN KEY(sid) REFERENCES stock
);