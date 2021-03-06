
    SET FOREIGN_KEY_CHECKS=0;

    drop table if exists avenant;

    drop table if exists contrat;

    drop table if exists contrathistorique;

    drop table if exists elementcontractuel;

    drop table if exists elementcontractuelfraiscontrat;

    drop table if exists elementcontractuelfraiscontrathistorique;

    drop table if exists elementcontractuelhistorique;

    drop table if exists fraiscontrat;

    drop table if exists keygen;

    drop table if exists modification;

    drop table if exists politiquecession;

    drop table if exists politiquemigration;

    drop table if exists politiquerenouvellement;

    drop table if exists politiqueresiliation;

    drop table if exists politiquevalidation;

    drop table if exists reduction;
    
     SET FOREIGN_KEY_CHECKS=1;

    create table avenant (
        id integer not null auto_increment,
        commentaire TINYTEXT,
        commentaireAnnulation TINYTEXT,
        dateAnnulation date,
        dateModification datetime,
        referenceContrat varchar(255),
        typeAvenant varchar(255),
        version integer,
        politiqueCessionId integer,
        politiqueMigrationId integer,
        politiqueRenouvellementId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table contrat (
        id integer not null auto_increment,
        dateDebutFacturation datetime,
        dateDerniereFacture datetime,
        dateFactureResiliation datetime,
        dateFinContrat datetime,
        datePreparation datetime,
        dateValidation datetime,
        idClient varchar(255),
        reference varchar(255),
        segmentTVA varchar(255),
        titre varchar(255),
        typeResiliation varchar(255),
        politiqueResiliationId integer,
        politiqueValidationId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table contrathistorique (
        id integer not null auto_increment,
        dateDebutFacturation datetime,
        dateDerniereFacture datetime,
        dateFactureResiliation datetime,
        dateFinContrat datetime,
        datePreparation datetime,
        dateValidation datetime,
        idClient varchar(255),
        reference varchar(255),
        segmentTVA varchar(255),
        titre varchar(255),
        typeResiliation varchar(255),
        version integer,
        politiqueResiliationId integer,
        politiqueValidationId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table elementcontractuel (
        id integer not null auto_increment,
        dateDebutFacturation datetime,
        dateDerniereFacture datetime,
        dateFactureResiliation datetime,
        dateFinContrat datetime,
        dateFinDuree datetime,
        dateFinEngagement datetime,
        datePreparation datetime,
        dateValidation datetime,
        duree integer,
        engagement integer,
        idAdrFacturation varchar(255),
        idAdrLivraison varchar(255),
        isMigre bit,
        modeFacturation varchar(255),
        modePaiement varchar(255),
        montant double precision,
        numEC integer,
        numeroCommande varchar(255),
        periodicite integer,
        referenceModePaiement varchar(255),
        referenceProduit varchar(255),
        referenceTarif varchar(255),
        remboursable bit,
        titre varchar(255),
        typeProduit varchar(255),
        typeResiliation varchar(255),
        typeTVA varchar(255),
        contratParent integer,
        dependDe integer,
        politiqueResiliationId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table elementcontractuelfraiscontrat (
        elementcontractuel_id integer not null,
        frais_id integer not null,
        primary key (elementcontractuel_id, frais_id)
    ) ENGINE=InnoDB;

    create table elementcontractuelfraiscontrathistorique (
        elementcontractuelhistorique_id integer not null,
        frais_id integer not null,
        primary key (elementcontractuelhistorique_id, frais_id)
    ) ENGINE=InnoDB;

    create table elementcontractuelhistorique (
        id integer not null auto_increment,
        dateDebutFacturation datetime,
        dateDerniereFacture datetime,
        dateFactureResiliation datetime,
        dateFinContrat datetime,
        dateFinDuree datetime,
        dateFinEngagement datetime,
        datePreparation datetime,
        dateValidation datetime,
        duree integer,
        engagement integer,
        idAdrFacturation varchar(255),
        idAdrLivraison varchar(255),
        isMigre bit not null,
        modeFacturation varchar(255),
        modePaiement varchar(255),
        montant double precision,
        numEC integer,
        numeroCommande varchar(255),
        periodicite integer,
        referenceModePaiement varchar(255),
        referenceProduit varchar(255),
        referenceTarif varchar(255),
        remboursable bit,
        titre varchar(255),
        typeProduit varchar(255),
        typeResiliation varchar(255),
        typeTVA varchar(255),
        contratParent integer,
        dependDe integer,
        politiqueResiliationId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table fraiscontrat (
        id integer not null auto_increment,
        montant double precision,
        nombreMois integer,
        ordre integer,
        titre varchar(255),
        typeFrais varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table keygen (
        id integer not null auto_increment,
        entite varchar(255),
        reference varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table modification (
        id integer not null auto_increment,
        numEC integer,
        trameJson longtext,
        avenantId integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table politiquecession (
        id integer not null auto_increment,
        conserverAncienneReduction bit,
        dateAction datetime,
        fraisCession double precision not null,
        montantRemboursement double precision,
        remboursement bit not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table politiquemigration (
        id integer not null auto_increment,
        dateAction date,
        fraisCreation bit not null,
        fraisMigration double precision,
        fraisResiliation bit not null,
        montantRemboursement double precision,
        montantResiliation double precision,
        penalite bit not null,
        reductionAncienne bit not null,
        remboursement bit not null,
        user varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table politiquerenouvellement (
        id integer not null auto_increment,
        conserverAncienneReduction bit,
        forceRenouvellement bit,
        user varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table politiqueresiliation (
        id integer not null auto_increment,
        commentaire TINYTEXT,
        commentaireAnnulation TINYTEXT,
        dateAnnulation date,
        dateRemboursement date,
        dateResiliation datetime,
        delaiDeSecurite bit,
        enCascade bit not null,
        fraisResiliation bit not null,
        montantRemboursement double precision,
        montantResiliation double precision,
        motif varchar(255),
        penalite bit not null,
        remboursement bit not null,
        typeResiliation varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table politiquevalidation (
        id integer not null auto_increment,
        checkIsPackagerCreationPossible bit,
        commentaire varchar(255),
        fraisCreation bit,
        primary key (id)
    ) ENGINE=InnoDB;

    create table reduction (
        id integer not null auto_increment,
        codeCatalogueReduction varchar(255),
        contextReduction varchar(255),
        dateAnnulation datetime,
        dateDebut datetime,
        dateFin datetime,
        isAffichableSurFacture bit,
        nbUtilisationEnCours integer,
        nbUtilisationMax integer,
        numEC integer,
        ordre integer,
        reference varchar(255),
        referenceContrat varchar(255),
        titre varchar(255),
        typeFrais varchar(255),
        typeReduction varchar(255),
        typeValeur varchar(255),
        valeur double precision,
        version integer,
        primary key (id)
    ) ENGINE=InnoDB;

    alter table avenant 
        add constraint UK_p7klicq1ffcn20d881hoqmdq4  unique (referenceContrat, version);

    create index index_id on contrat (id);

    create index index_reference on contrat (reference);

    create index index_titre on contrat (titre);

    create index index_id on elementcontractuel (id);

    create index index_contratParent on elementcontractuel (contratParent);

    create index index_numEC on elementcontractuel (numEC);

    create index index_referenceProduit on elementcontractuel (referenceProduit);

    alter table elementcontractuelfraiscontrat 
        add constraint UK_tofj2amcpxxti50qok6g54sx3  unique (frais_id);

    alter table elementcontractuelfraiscontrathistorique 
        add constraint UK_9pvhr83hkq6ethd3n09mr3uwf  unique (frais_id);

    alter table reduction 
        add constraint UK_c3h66qghv6tpby6ids3dtibxq  unique (referenceContrat, version, numEC, contextReduction);
