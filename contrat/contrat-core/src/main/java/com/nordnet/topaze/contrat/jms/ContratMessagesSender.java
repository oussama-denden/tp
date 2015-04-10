package com.nordnet.topaze.contrat.jms;

import java.util.Map;

import com.nordnet.topaze.client.rest.business.contrat.Contrat;

/**
 * ActiveMQ message sender.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface ContratMessagesSender {

	/**
	 * Evenement a envoyer lorsque le contrat est validee.
	 * 
	 * @param contratValidatedInfo
	 *            les inforamation envoye dans un evenement 'contratValidatedEvent'.
	 */
	public void sendContratValidatedEvent(Map<String, Object> contratValidatedInfo);

	/**
	 * Set date debut facturation afin de commencer la facturation.
	 * 
	 * @param referenceContrat
	 *            refernce du contrat.
	 */
	public void sendLaunchBillingEvent(String referenceContrat);

	/**
	 * Evenement a envoyer lorsque le contrat est resilier.
	 * 
	 * @param referenceContrat
	 *            reference du Contrat.
	 * @param isResiliationPartiel
	 *            true si la resiliation est partiel.
	 */
	public void sendContractResiliatedEvent(String referenceContrat, Integer numEC, boolean isResiliationPartiel);

	/**
	 * Evenement a envoyer pour effectuer le dernier billing apres la resiliation d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 */

	public void sendContractPartialResiliatedEvent(String referenceContrat, Integer numEC);

	/**
	 * Evenement a envoyer lorsque le contrat est migre.
	 * 
	 * @param referenceContratMigrer
	 *            reference du contrat.
	 */
	public void sendContratMigratedEvent(String referenceContratMigrer);

	/**
	 * Evenement a envoyer lorsque le contrat est migre pour la facturation.
	 * 
	 * @param referenceContratMigrer
	 *            reference du contrat.
	 */
	public void sendContratMigratedBillingEvent(String referenceContratMigre);

	/**
	 * evenement envoyer vers la facturation pour la facturation des frais de cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	public void sendContratSuccessionBillingEvent(String referenceContrat);

	/**
	 * evenement envoyer vers la businessprocess-livraison pour lancer la cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	public void sendContratSuccessionEvent(String referenceContrat);

	/**
	 * evenement envoyer vers la facturation pour la facturation des frais de remboursement et lancer le billing
	 * recurrent.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	public void sendContratMigrationLivredBillingEvent(String referenceContrat);

	/**
	 * envoi de l'evenement 'LunchSuccessionBilling' pour lancer les frais de remboursement a l'ancient client et le
	 * billing recurrent au nouveau client.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	public void sendLunchSuccessionBillingEvent(String referenceContrat);

	/**
	 * envoi de l'evenement ContractRenewalpour infomrer de la renouvellement du contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	public void sendContractRenewalEvent(String referenceContrat);

	/**
	 * envoi de l'evenement 'LunchSuccessionBilling' pour lancer les frais de remboursement a l'ancient client et le
	 * billing recurrent au nouveau client.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	public void sendLunchRenewalBillingEvent(String referenceContrat);

	/**
	 * envoi de l'evenement 'ContratDelivered'.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}.
	 */
	public void sendContractDeliveredEvent(String referenceContrat);

	public void sendControlVenteEvent(Map<String, Object> contratValideInfo);

}
