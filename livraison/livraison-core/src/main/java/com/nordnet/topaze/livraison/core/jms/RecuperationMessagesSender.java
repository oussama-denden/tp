package com.nordnet.topaze.livraison.core.jms;

import java.util.Map;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;

/**
 * ActiveMQ message sender.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface RecuperationMessagesSender {

	/**
	 * Evenement envoyer suite a l'initiation d'un bon de retour.
	 * 
	 * @param bonRecuperation
	 *            bon retour.
	 */
	void sendReturnInitiatedEvent(BonPreparation bonRecuperation);

	/**
	 * Evenement envoyer suite a des element de retour apres une resiliation partiel.
	 * 
	 * @param bonRecuperation
	 *            bon retour.
	 */
	void sendSubReturnInitiatedEvent(BonPreparation bonRecuperation);

	/**
	 * Envoyer un evenement pour faire la traduction d'un BR.
	 * 
	 * @param bonRecuperationGlobal
	 *            le {@link BonPreparation} a preparer.
	 */
	public void sendPrepareReturnEvent(BonPreparation bonRecuperationGlobal);

	/**
	 * Evenement a envoyer suite a la recuperation d'un bien ou d'une service.
	 * 
	 * @param recuperationEvent
	 *            informations a communiquer avec l'event.
	 */
	public void sendReturnReturnedEvent(Map<String, Object> recuperationEvent);

}
