DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS account;

-- Person Table
CREATE TABLE `customer` (
    `id`	INTEGER NOT NULL UNIQUE,
    `firstname`	TEXT NOT NULL,
    `lastname`	TEXT NOT NULL,
    `email`	TEXT NOT NULL UNIQUE,
    `date_of_birth`	TEXT NOT NULL,
    `age` INTEGER NOT NULL,
    `phone_number` INTEGER,
    `SSN` TEXT,  -- TODO (shubham) should be masked when storing on database
    `password`	TEXT NOT NULL,
    PRIMARY KEY(id)
);

insert into customer (id, firstname, lastname, email, date_of_birth, age, password) values (1, 'admin', 'admin', 'admin@mail.com', '01/01/2000', 30, 'admin');

select * from customer;

-- Account Table
CREATE TABLE `account` (
    `id` INTEGER NOT NULL UNIQUE,
    `cid` INTEGER NOT NULL,
    `account_no` INTEGER NOT NULL,
    `account_type` TEXT NOT NULL,   -- TODO (shubham) put a check constraint for 'saving', 'checking'
    `balance` REAL,   -- TODO (shubham) put a check constraint for MIN balance for account
    PRIMARY KEY(id), FOREIGN KEY(cid) REFERENCES customer
);