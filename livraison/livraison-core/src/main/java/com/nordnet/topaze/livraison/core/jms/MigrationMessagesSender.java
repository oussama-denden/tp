package com.nordnet.topaze.livraison.core.jms;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;

/**
 * ActiveMQ message sender.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface MigrationMessagesSender {

	/**
	 * Envoyer un evenement apres l'initiation de migration.
	 * 
	 * @param bonMigration
	 *            le bon de migration.
	 */
	public void sendMigrationInitiatedEvent(BonPreparation bonMigration);

	/**
	 * Envoyer un evenement une fois que l {@link ElementLivraison} de type migration est marque comme livre.
	 * 
	 * @param sousBM
	 *            le sous {@link BonPreparation} marque comme livre.
	 */
	public void sendSubDeliveryMigrationDeliveredEvent(BonPreparation sousBM);

	/**
	 * Envoyer un evenement une fois que l {@link ElementLivraison} de type migration est marque comme termine.
	 * 
	 * @param sousBM
	 *            le sous {@link BonPreparation} marque comme livre.
	 */
	public void sendSubDeliveryMigrationReturnedEvent(BonPreparation sousBM);

	/**
	 * * Envoyer un evenement une fois que le bon de migration global est marque comme livre.
	 * 
	 * @param referenceContrat
	 *            reference du contrat associe.
	 */
	public void sendMigrationDeliveredEvent(String referenceContrat);

	/**
	 * Envoyer un evenement une fois que l {@link BonPreparation} de type migration est marque comme retourne.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 */
	public void sendMigrationReturnedEvent(String referenceContrat);

	/**
	 * Envoyer un evenement pour faire la traduction d'un BM.
	 * 
	 * @param bonMigration
	 *            le {@link BonPreparation} de migration a preparer.
	 */
	public void sendPrepareMigrationEvent(BonPreparation bonMigration);

	/**
	 * Envoyer un evenement une fois que la preparation d'un {@link ElementLivraison} est faite pour verifier si le BP
	 * global est preparer ou non.
	 * 
	 * @param bonMigration
	 *            {@link BonPreparation}.
	 */
	public void sendSubDeliveryPreparedEvent(BonPreparation bonMigration);

	/**
	 * envoi d'un evenement 'PrepareSuccession'.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 */
	public void sendPrepareSuccessionEvent(BonPreparation bonPreparation);

	/**
	 * envoyer un evenement aprés cession d'un bien/service.
	 * 
	 * @param referenceBonPreparation
	 *            reference {@link BonPreparation}.
	 */
	public void sendSubDeliverySuccessedEvent(String referenceBonPreparation);

	/**
	 * envoyer evenement apres cession du bon global.
	 * 
	 * @param referenceBonPreparation
	 *            reference bon preparation.
	 */
	public void sendDeliverySuccessedEvent(String referenceBonPreparation);

	/**
	 * envoyer evenement apres la fin du livraison de bon migration global.
	 * 
	 * @param referenceBonMigration
	 *            reference bon migration.
	 */
	public void sendMigrationDelivredEvent(String referenceBonMigration);

	/**
	 * envoyer evenement apres le retour complet du bon de migration.
	 * 
	 * @param referenceBonMigration
	 *            reference bon migration.
	 */
	public void sendMigrationReturnedBillingEvent(String referenceBonMigration);

	/**
	 * envoi d'un evenement 'PrepareRenewal'.
	 * 
	 * @param bonRenouvellement
	 *            bon de renouvellement
	 */
	public void sendPrepareRenewalEvent(BonPreparation bonRenouvellement);

	/**
	 * envoyer evenement apres renouvellement du bon global.
	 * 
	 * @param referenceBonRenouvellement
	 *            reference bon renouvellement.
	 */
	public void sendDeliveryRenewedEvent(String referenceBonRenouvellement);

	/**
	 * envoyer un evenement aprés renouvellement d'un bien/service.
	 * 
	 * @param referenceBonRenouvellement
	 *            reference {@link BonPreparation}.
	 */
	public void sendSubDeliveryRenewedEvent(String referenceBonRenouvellement);

}
