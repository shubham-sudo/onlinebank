CREATE TABLE `person` (
    `id`	INTEGER NOT NULL UNIQUE,
    `firstname`	TEXT NOT NULL,
    `lastname`	TEXT NOT NULL,
    `email`	TEXT NOT NULL UNIQUE,
    `dateofbirth`	TEXT NOT NULL,
    `password`	TEXT NOT NULL,
    PRIMARY KEY(id)
);

insert into person (?, ?, ?, ?, ?, ?) values (1, 'admin', 'admin', 'admin@mail.com', '01/01/2000', 'admin');

select * from person;