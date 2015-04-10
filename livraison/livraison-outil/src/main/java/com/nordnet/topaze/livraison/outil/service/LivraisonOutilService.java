package com.nordnet.topaze.livraison.outil.service;

import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe regroupe l'ensemble des methodes de l'outil livraison.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface LivraisonOutilService {

	/**
	 * @param referenceBP
	 *            reference du contrat.
	 * @return retourne le bon de preparation d'un contrat global.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonPreparation(String referenceBP) throws TopazeException;

	/**
	 * Recuperer les informations de migration d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratMigrationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratMigrationInfo getContratMigrationInfo(String referenceContrat) throws TopazeException;

}
