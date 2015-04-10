package com.nordnet.topaze.businessprocess.swap.service;

import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe contient les methodes relative a Swap.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */

public interface SwapService {

	/**
	 * consulter l'etat du l'element de migration pour verifier s'il est livre.
	 * 
	 * @param elementMigration
	 *            {@link ElementMigration}
	 * @return true si em livre.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public boolean consulterEtatLivraisonEM(ElementLivraison elementMigration) throws TopazeException;

	/**
	 * consulter l'etat du l'element de migration vérifier s'il est retourné.
	 * 
	 * @param elementMigration
	 *            {@link ElementLivraison}.
	 * @return true si em retourne.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public boolean consulterEtatRetourEM(ElementLivraison elementMigration) throws TopazeException;

	/**
	 * Chercher le numero de serie d'un produit a partir de NetEquipement.
	 * 
	 * @param idClient
	 *            l'id de client.
	 * @param refContrat
	 *            reference du contrat.
	 * @return numero de serie.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String recupererSerialNumber(String idClient, String refContrat) throws TopazeException;

	/**
	 * Creer une demande d'echange d'equipement a traver Swap.
	 * 
	 * @param bonMigration
	 *            le bon de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionSwap(BonPreparation bonMigration) throws TopazeException;

	/**
	 * Creer une demande d'echange d'equipement a traver Swap.
	 * 
	 * @param referenceBP
	 *            reference de bon de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionSwap(String referenceBP) throws TopazeException;

	/**
	 * ceder les equipements aux nouveau client.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void cederEquipement(BonPreparation bonPreparation) throws TopazeException;

	/**
	 * 
	 * @param referenceBP
	 * @throws TopazeException
	 */
	/**
	 * ceder un equipement.
	 * 
	 * @param referenceBP
	 *            reference bon preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void cederEquipement(String referenceBP) throws TopazeException;

}
