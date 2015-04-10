package com.nordnet.topaze.livraison.core.service;

import java.util.List;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;

/**
 * Cette classe va contenir toutes les methodes en rapport avec le bon de preparation pour le retour.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface BonRecuperationService {

	/**
	 * Editer un bon de préparation Afin de répertorier les éléments que le client doit retourner, et les services a
	 * désactiver.
	 * 
	 * @param reference
	 *            reference du contrat.
	 * @throws TopazeException
	 *             the description bon preparation non valide exception {@link TopazeException}.
	 */
	public void initierBR(String reference) throws TopazeException;

	/**
	 * Initier un element de retour apres une resiliation partiel.
	 * 
	 * @param reference
	 *            reference {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierER(String reference) throws TopazeException;

	/**
	 * Preparer un {@link BonPreparation} deja initié. Sa entraine d'emettre les appels vers Packager et vers NetRetour.
	 * 
	 * @param bonRecuperation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBR(final BonPreparation bonRecuperation) throws TopazeException;

	/**
	 * Preparer les {@link ElementLivraison} deja initié. Sa entraine d'emettre les appels vers Packager et vers
	 * NetRetour.
	 * 
	 * @param bonRecuperation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerER(final BonPreparation bonRecuperation) throws TopazeException;

	/**
	 * Marquer un BP de recuperation que se soit global, bien ou service comme recuperer.
	 * 
	 * @param referenceBR
	 *            reference de BR.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerRecupere(String referenceBR) throws TopazeException;

	/**
	 * @return list des sous {@link BonPreparation} de type bien non recupere.
	 */
	public List<ElementLivraison> getBiensEnCoursRecuperation();

	/**
	 * retourne la liste des {@link ElementLivraison} de type service en cours de recuperation.
	 * 
	 * @return la list des {@link ElementLivraison} de type service en cours de recuperation.
	 */
	public List<ElementLivraison> getServicesEnCoursSuspension();

	/**
	 * retourne la liste des sous {@link BonPreparation} de type service qui sont suspendu.
	 * 
	 * @return la list des sous {@link BonPreparation} de type service qui sont suspendu.
	 */
	public List<ElementLivraison> getServicesSuspendu();

	/**
	 * @return liste de BR Global non recupere.
	 */
	public List<BonPreparation> getBRGlobalEncoursRecuperation();

	/**
	 * Marquer un EL comme recuperer.
	 * 
	 * @param referenceEL
	 *            reference de l'element de livraison a marquer comme recuperer.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void marquerELRecupere(String referenceEL) throws TopazeException;

	/**
	 * Chercher un Bon de prparation par reference.
	 * 
	 * @param referenceBP
	 *            reference du bon de preparation.
	 * @return {@link BonPreparation} de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation findBRByReference(String referenceBP) throws TopazeException;

	/**
	 * Chercher un element de recuperation par reference.
	 * 
	 * @param referenceER
	 *            reference du ER.
	 * @return {@link ElementLivraison} de recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public ElementLivraison findElementRecuperation(String referenceER) throws TopazeException;

}
