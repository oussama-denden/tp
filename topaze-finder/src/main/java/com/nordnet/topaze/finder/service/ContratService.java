package com.nordnet.topaze.finder.service;

import java.util.List;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.business.espaceclient.EspaceClientAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.Tarif;
import com.nordnet.topaze.finder.business.welcome.WelcomeAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.WelcomeContratInfo;

/**
 * La service ContratService va contenir tous les operations en rapport avec le contrat.
 * 
 * @author anisselmane.
 * 
 */
public interface ContratService {

	/**
	 * Chercher les abonnements en cours pour un client.
	 * 
	 * @param idClient
	 *            id du client.
	 * @return {@link WelcomeAbonnementInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	List<WelcomeAbonnementInfo> getWelcomeAbonnementsClient(String idClient) throws TopazeException;

	/**
	 * Chercher les abonnements en cours d'un client pour l'Espace Client .
	 * 
	 * @param idClient
	 *            id du client.
	 * @return {@link EspaceClientAbonnementInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	List<EspaceClientAbonnementInfo> getEspaceClientAbonnementsClient(String idClient) throws TopazeException;

	/**
	 * Recuperer les informations detaillee du contrat pour Welcome.
	 * 
	 * @param refContrat
	 *            reference du contrat.
	 * @return {@link WelcomeContratInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	WelcomeContratInfo getWelcomeContratInfo(String refContrat) throws TopazeException;

	/**
	 * Information de tarification du contrat.
	 * 
	 * @param refContrat
	 *            reference du contrat.
	 * @return {@link Tarif}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	List<Tarif> getTarifs(String refContrat) throws TopazeException;

	/**
	 * Recuperer le Retailer Packager ID.
	 * 
	 * @param refContrat
	 *            reference du contrat.
	 * @return Retailer Packager ID.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	String getRPID(String refContrat) throws TopazeException;

}