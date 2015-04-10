package com.nordnet.topaze.communication.service;

import com.nordnet.topaze.exception.TopazeException;

/**
 * service responsable d'envoyer un mail au client lors de la livraison du contrat.
 * 
 * @author akram-moncer
 * 
 */
public interface CommunicationService {

	/**
	 * envoyer un email au client pour l'informer de la livraison du contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void envoyerMail(String referenceContrat) throws TopazeException;

}
