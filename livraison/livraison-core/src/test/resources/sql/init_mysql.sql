
	SET FOREIGN_KEY_CHECKS=0;
	
    drop table if exists bonpreparation;

    drop table if exists elementlivraison;
    
    	SET FOREIGN_KEY_CHECKS=1;

    create table bonpreparation (
        id integer not null auto_increment,
        causeNonlivraison varchar(255),
        dateDebutPreparation datetime,
        dateInitiation datetime,
        dateLivraisonTermine datetime,
        datePreparation datetime,
        dateRetourTermine datetime,
        dateAnnulation datetime,
        idClient varchar(255),
        reference varchar(255),
        typeBonPreparation varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table elementlivraison (
        id integer not null auto_increment,
        acteur varchar(255),
        addresseLivraison varchar(255),
        ancienCodeColis varchar(255),
        ancienRetailerPackagerId varchar(255),
        causeNonlivraison varchar(255),
        codeColis varchar(255),
        dateDebutPreparation datetime,
        dateInitiation datetime,
        dateLivraisonTermine datetime,
        datePreparation datetime,
        dateRetourTermine datetime,
        numEC integer,
        reference varchar(255),
        referenceAncienProduit varchar(255),
        referenceProduit varchar(255),
        resiliationPartiel bit,
        retailerPackagerId varchar(255),
        typeBonPreparation varchar(255),
        typeContrat varchar(255),
        bonPreparationParent integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create index index_numEC on elementlivraison (numEC);

    create index index_reference on elementlivraison (reference);

    create index index_referenceProduit on elementlivraison (referenceProduit);

    alter table elementlivraison 
        add constraint FK_g0rk8li35c2wtdn2ra9itxf5n 
        foreign key (bonPreparationParent) 
        references bonpreparation (id);
