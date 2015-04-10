package com.nordnet.topaze.livraison.core.jms;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;

/**
 * ActiveMQ message sender.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface LivraisonMessagesSender {

	/**
	 * Envoyer un evenement apres l'initiation d'un bon de preparation.
	 * 
	 * @param bonPreparationGlobal
	 *            le {@link BonPreparation} global initie.
	 */
	public void sendDeliveryInitiatedEvent(BonPreparation bonPreparationGlobal);

	/**
	 * Envoyer un evenement pour faire la traduction d'un BP.
	 * 
	 * @param bonPreparationGlobal
	 *            le {@link BonPreparation} a preparer.
	 */
	public void sendPrepareDeliveryEvent(BonPreparation bonPreparationGlobal);

	/**
	 * Envoyer un evenement une fois que la preparation d'un {@link ElementLivraison} est faite pour verifier si le BP
	 * global est preparer ou non.
	 * 
	 * @param bonPreparationGlobal
	 *            {@link BonPreparation}.
	 */
	public void sendSubDeliveryPreparedEvent(BonPreparation bonPreparationGlobal);

	/**
	 * Envoyer un evenement une fois que l {@link ElementLivraison} est marque comme livre.
	 * 
	 * @param sousBP
	 *            le sous {@link BonPreparation} marque comme livre.
	 */
	public void sendSubDeliveryDeliveredEvent(BonPreparation sousBP);

	/**
	 * Envoyer un evenement une fois que le bon de preparation global est marque comme livre.
	 * 
	 * @param referenceContrat
	 *            reference du contrat associe.
	 */
	public void sendDeliveryDeliveredEvent(String referenceContrat);

}
