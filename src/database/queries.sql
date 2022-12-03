DROP TABLE IF EXISTS person;

-- Person Table
CREATE TABLE `person` (
    `id`	INTEGER NOT NULL UNIQUE,
    `firstname`	TEXT NOT NULL,
    `lastname`	TEXT NOT NULL,
    `email`	TEXT NOT NULL UNIQUE,
    `date_of_birth`	TEXT NOT NULL,
    `age` INTEGER NOT NULL,
    `password`	TEXT NOT NULL,
    PRIMARY KEY(id)
);

insert into person (id, firstname, lastname, email, date_of_birth, age, password) values (1, 'admin', 'admin', 'admin@mail.com', '01/01/2000', 30, 'admin');

-- select * from person;

-- Account Table
CREATE TABLE `account` (
    `id` INTEGER NOT NULL UNIQUE,
    `account_no` INTEGER NOT NULL,
    `account_type` TEXT NOT NULL,   -- TODO (shubham) put a check constraint for 'saving', 'checking'
    `balance` DOUBLE PRECISION,   -- TODO (shubham) put a check constraint for MIN balance for account
    PRIMARY KEY(id)
);