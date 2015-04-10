package com.nordnet.topaze.businessprocess.netretour.service;

import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe contient les methode relative a NetRetour.
 * 
 * @author akram-moncer
 * 
 */
public interface NetRetourService {

	/**
	 * verifier si un bien est retourne ou pas.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @return true si le bien est retourne.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	Boolean checkBienRecupere(ElementLivraison elementLivraison) throws TopazeException;

	/**
	 * Une méthode appelant NetRetour afin d'éditer un Bon de Retour des biens.
	 * 
	 * @param sousBR
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 * 
	 */
	void traductionNetRetour(BonPreparation sousBR) throws TopazeException;

	/**
	 * Une méthode appelant NetRetour afin d'éditer un Bon de renouvellement des biens.
	 * 
	 * @param bonRenouvellement
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	void activerBienRenouvellement(BonPreparation bonRenouvellement) throws TopazeException;

	/**
	 * Une méthode appelant NetRetour afin d'éditer un Bon de renouvellement des biens.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	void activerBienRenouvellement(String referenceBP) throws TopazeException;

	/**
	 * Traduction de bon de retour vers NetRetour.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @param typeBonPreparation
	 *            {@link TypeBonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	void traductionNetRetour(String referenceBP, TypeBonPreparation typeBonPreparation) throws TopazeException;

}
