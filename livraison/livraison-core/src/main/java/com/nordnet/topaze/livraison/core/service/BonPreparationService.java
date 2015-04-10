package com.nordnet.topaze.livraison.core.service;

import java.util.List;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;

/**
 * Cette classe va contenir toutes les methodes en rapport avec le bon de livraison.
 * 
 * @author akram-moncer
 * 
 */
public interface BonPreparationService {

	/**
	 * 
	 * @param bonPreparationGlobal
	 *            un {@link BonPreparation} cree par la Business Process.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	public void initierBP(BonPreparation bonPreparationGlobal) throws TopazeException;

	/**
	 * 
	 * @param referenceBPGlobal
	 *            reference du Bon de preparation Global.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBP(String referenceBPGlobal) throws TopazeException;

	/**
	 * 
	 * @param referenceBPGlobal
	 *            reference du Bon de preparation Global a preparer.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalPrepare(String referenceBPGlobal) throws TopazeException;

	/**
	 * 
	 * @param bonPreparationGlobal
	 *            le {@link BonPreparation} global dont les biens seront marquer comme prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBienPreparer(BonPreparation bonPreparationGlobal) throws TopazeException;

	/**
	 * 
	 * @param elementLivraison
	 *            le {@link ElementLivraison} marquer comme prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerServicePreparer(ElementLivraison elementLivraison) throws TopazeException;

	/**
	 * 
	 * @param elementLivraison
	 *            l'{@link ElementLivraison} a marquer comme livre.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerLivre(ElementLivraison elementLivraison) throws TopazeException;

	/**
	 * 
	 * @param referenceBPGlobal
	 *            la reference du bon de preparation global a marquer comme livre.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerLivre(String referenceBPGlobal) throws TopazeException;

	/**
	 * 
	 * @param referenceSousBP
	 *            la reference du sous bon de preparation a marquer non livrer.
	 * @param causeNonLivraison
	 *            la cause de non livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerSousBPNonLivre(String referenceSousBP, String causeNonLivraison) throws TopazeException;

	/**
	 * annuler un BP.
	 * 
	 * @param referenceBPGlobal
	 *            reference {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void annulerBonPreparation(String referenceBPGlobal) throws TopazeException;

	/**
	 * 
	 * @param reference
	 *            reference du bon de preparation.
	 * @return {@link BonPreparation}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation findByReference(String reference) throws TopazeException;

	/**
	 * 
	 * @param reference
	 *            reference de l'element de livraison.
	 * @param referenceProduit
	 *            reference produit.
	 * @return {@link ElementLivraison}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ElementLivraison findElementLivraisonByReferenceAndReferenceProduit(String reference, String referenceProduit)
			throws TopazeException;

	/**
	 * retourne la liste des {@link ElementLivraison} de type service en cours d'activation.
	 * 
	 * @return liste des sous {@link ElementLivraison} de type service en cours de livraison.
	 */
	public List<ElementLivraison> getServicesEnCoursActivation();

	/**
	 * retourne la liste des sous {@link ElementLivraison} de type bien en cours de livraison.
	 * 
	 * @return liste des sous {@link ElementLivraison} de type type bien en cours de livraison.
	 */
	public List<ElementLivraison> getBiensEnCoursLivraison();

	/**
	 * Chercher un Bon de prparation par reference.
	 * 
	 * @param referenceBP
	 *            reference du bon de preparation.
	 * @return {@link BonPreparation} de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation findBPByReference(String referenceBP) throws TopazeException;

	/**
	 * Chercher un Bon de prparation parent par reference.
	 * 
	 * @param referenceEL
	 *            reference du bon de preparation.
	 * @param referenceProduit
	 *            reference produit.
	 * @param isRetour
	 *            true si retour.
	 * @return {@link BonPreparation} de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String getReferenceBonPreparationParent(String referenceEL, String referenceProduit, Boolean isRetour)
			throws TopazeException;

}