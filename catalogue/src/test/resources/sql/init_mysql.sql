
    alter table fraisproduit 
        drop 
        foreign key FK_1jowjjdepn36k90rqfjip60pm;

    alter table prix 
        drop 
        foreign key FK_366jsiafa4mkkscxm6tqccbfj;

    drop table if exists fraisproduit;

    drop table if exists prix;

    drop table if exists produit;

    create table fraisproduit (
        id integer not null auto_increment,
        montant double precision,
        typeFrais varchar(255),
        produitId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table prix (
        id integer not null auto_increment,
        engagement integer,
        montant double precision,
        typePrix varchar(255),
        produitId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table produit (
        id integer not null auto_increment,
        delaisPreparation integer,
        label varchar(255),
        outilsLivraison varchar(255),
        reference varchar(255),
        typeProduit varchar(255),
        typeTVA varchar(255),
        xmlTemplatePath varchar(255),
        xsdTemplatePath varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    alter table fraisproduit 
        add constraint FK_1jowjjdepn36k90rqfjip60pm 
        foreign key (produitId) 
        references produit (id);

    alter table prix 
        add constraint FK_366jsiafa4mkkscxm6tqccbfj 
        foreign key (produitId) 
        references produit (id);
