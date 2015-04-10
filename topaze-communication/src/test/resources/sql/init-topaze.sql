--
-- Drop schema topaze_test
--

DROP DATABASE IF EXISTS topaze_test;
commit;
--
-- Create schema topaze_test
--

CREATE DATABASE IF NOT EXISTS topaze_test;
USE topaze_test;

--
-- Table structure for table `bonpreparation`
--

DROP TABLE IF EXISTS `bonpreparation`;
CREATE TABLE `bonpreparation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addressLivraison` varchar(255) DEFAULT NULL,
  `codeColis` varchar(255) DEFAULT NULL,
  `codeProduit` varchar(255) DEFAULT NULL,
  `dateInitiation` datetime DEFAULT NULL,
  `dateLivraison` datetime DEFAULT NULL,
  `dateDebutPreparation` datetime DEFAULT NULL,
  `datePreparation` datetime DEFAULT NULL,
  `dateRecuperation` datetime DEFAULT NULL,
  `idClient` varchar(255) DEFAULT NULL,
  `outilLivraison` varchar(255) DEFAULT NULL,
  `reference` varchar(255) NOT NULL,
  `referenceProduit` varchar(255) DEFAULT NULL,
  `typeBonPreparation` varchar(255) DEFAULT NULL,
  `etatBonPreparation` varchar(255) DEFAULT NULL,
  `bonPreparationParent` int(11) DEFAULT NULL,
   `causeNonlivraison` longtext,
  PRIMARY KEY (`id`)
--  KEY `FK_btnkbe5o5y01rh9pdq8yf0btm` (`bonPreparationParent`),
--  CONSTRAINT `FK_btnkbe5o5y01rh9pdq8yf0btm` FOREIGN KEY (`bonPreparationParent`) REFERENCES `bonpreparation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `contrat`
--

DROP TABLE IF EXISTS `contrat`;
CREATE TABLE `contrat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateDebutFacturation` datetime DEFAULT NULL,
  `dateValidation` datetime DEFAULT NULL,
  `engagement` int(11) DEFAULT NULL,
  `idAdrFacturation` varchar(255) DEFAULT NULL,
  `idAdrLivraison` varchar(255) DEFAULT NULL,
  `idClient` varchar(255) DEFAULT NULL,
  `modeFacturation` varchar(255) DEFAULT NULL,
  `reference` varchar(255) NOT NULL,
  `referenceProduit` varchar(255) DEFAULT NULL,
  `titre` varchar(255) NOT NULL,
  `typeContrat` varchar(255) NOT NULL,
  `contratParent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_o4dywik7c5c4uatxa04sbxwiw` (`contratParent`),
  CONSTRAINT `FK_o4dywik7c5c4uatxa04sbxwiw` FOREIGN KEY (`contratParent`) REFERENCES `contrat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `fraiscontrat`
--

DROP TABLE IF EXISTS `fraiscontrat`;
CREATE TABLE `fraiscontrat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `montant` double DEFAULT NULL,
  `contratId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_3d2m9nbt267t9uhdku4m1hdc0` (`contratId`),
  CONSTRAINT `FK_3d2m9nbt267t9uhdku4m1hdc0` FOREIGN KEY (`contratId`) REFERENCES `contrat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `produit`
--

DROP TABLE IF EXISTS `produit`;
CREATE TABLE `produit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `outilsLivraison` varchar(255) NOT NULL,
  `reference` varchar(255) NOT NULL,
  `typeProduit` varchar(255) NOT NULL,
  `typeTVA` varchar(255) NOT NULL,
  `xmlTemplatePath`  varchar(255),
  `xsdTemplatePath`  varchar(255),
  `delaisPreparation` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `prix`
--

DROP TABLE IF EXISTS `prix`;
CREATE TABLE `prix` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `engagement` int(11) DEFAULT NULL,
  `montant` double NOT NULL,
  `typePrix` varchar(255) NOT NULL,
  `produitId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gakmmh5wtxidnjm2fu94ijvlw` (`produitId`),
  CONSTRAINT `FK_gakmmh5wtxidnjm2fu94ijvlw` FOREIGN KEY (`produitId`) REFERENCES `produit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `fraisproduit`
--

DROP TABLE IF EXISTS `fraisproduit`;
CREATE TABLE `fraisproduit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `montant` double DEFAULT NULL,
  `produitId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_g7fxmgfrr9qcdqncuw4sshvn0` (`produitId`),
  CONSTRAINT `FK_g7fxmgfrr9qcdqncuw4sshvn0` FOREIGN KEY (`produitId`) REFERENCES `produit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `tarif`
--

DROP TABLE IF EXISTS `tarif`;
CREATE TABLE `tarif` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modePaiement` varchar(255) DEFAULT NULL,
  `montant` double NOT NULL,
  `referenceModePaiement` varchar(255) DEFAULT NULL,
  `typePrix` varchar(255) NOT NULL,
  `contratId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lmfoquv50plp3hmqymxii761n` (`contratId`),
  CONSTRAINT `FK_lmfoquv50plp3hmqymxii761n` FOREIGN KEY (`contratId`) REFERENCES `contrat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;