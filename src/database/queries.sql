DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS account;

-- Customer Table
CREATE TABLE `customer` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `firstname`	TEXT NOT NULL,
    `lastname` TEXT NOT NULL,
    `email` TEXT NOT NULL UNIQUE,
    `date_of_birth`	TEXT NOT NULL,
    `age` INTEGER NOT NULL,
    `phone_number` INTEGER,
    `SSN` TEXT,  -- TODO (shubham) should be masked when storing on database
    `password`	TEXT NOT NULL
);

insert into customer (id, firstname, lastname, email, date_of_birth, age, password) values (1, 'admin', 'admin', 'admin@mail.com', '01/01/2000', 30, 'admin');

-- Account Table
CREATE TABLE `account` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `cid` INTEGER NOT NULL,
    `account_no` INTEGER NOT NULL UNIQUE,
    `account_type` TEXT NOT NULL,   -- TODO (shubham) put a check constraint for 'saving', 'checking'
    `balance` REAL,   -- TODO (shubham) put a check constraint for MIN balance for account
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
    FOREIGN KEY(cid) REFERENCES customer
)