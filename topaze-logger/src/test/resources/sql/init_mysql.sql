
    drop table if exists tracage;

    create table tracage (
        id integer not null auto_increment,
        date datetime,
        descr longtext,
        ip varchar(255),
        reference varchar(255),
        target varchar(255),
        type varchar(255),
        user varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;
