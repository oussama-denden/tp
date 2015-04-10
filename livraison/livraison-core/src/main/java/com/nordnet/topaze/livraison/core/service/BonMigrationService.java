package com.nordnet.topaze.livraison.core.service;

import java.util.List;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.business.ElementStateInfo;
import com.nordnet.topaze.livraison.core.business.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;

/**
 * Cette classe contient toutes les methodes en rapport avec la migration.
 * 
 * @author Oussama Denden
 * 
 */
public interface BonMigrationService {

	/**
	 * Initier un bon de migration.
	 * 
	 * @param bonMigration
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBM(BonPreparation bonMigration) throws TopazeException;

	/**
	 * Chercher un Bon de migration par reference.
	 * 
	 * @param reference
	 *            reference du bon de migration.
	 * @return {@link BonPreparation}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation findBMByReference(String reference) throws TopazeException;

	/**
	 * Chercher un élément de migration par reference.
	 * 
	 * @param reference
	 *            reference du bon de migration.
	 * @return {@link BonPreparation}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ElementLivraison findEMByReference(String reference) throws TopazeException;

	/**
	 * retourne la liste des sous {@link ElementLivraison} de type bien en cours de migration.
	 * 
	 * @return liste des sous {@link ElementLivraison} de type type bien en cours de migration.
	 */
	public List<ElementLivraison> getBiensMigrationEnCoursLivraison();

	/**
	 * 
	 * @param elementLivraison
	 *            l'{@link ElementLivraison} a marquer un element de migration comme livre.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerEMLivre(ElementLivraison elementLivraison) throws TopazeException;

	/**
	 * Marquer un BP de Migration comme livré.
	 * 
	 * @param referenceBM
	 *            reference de BM.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMGlobalLivre(String referenceBM) throws TopazeException;

	/**
	 * retourne la liste des sous {@link ElementLivraison} migration de type bien en cours de retour.
	 * 
	 * @return liste des sous {@link ElementLivraison} migration de type type bien en cours de retour.
	 */
	public List<ElementLivraison> getBiensMigrationEnCoursRetour();

	/**
	 * Marquer un BP de Migration comme retourné.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerEMRetourne(final ElementLivraison elementLivraison) throws TopazeException;

	/**
	 * Marquer un BM de Migration comme Retourne.
	 * 
	 * @param referenceBM
	 *            reference de BM.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMGlobalRetourne(String referenceBM) throws TopazeException;

	/**
	 * Preparation d'un bon de migration.
	 * 
	 * @param referenceBM
	 *            reference du Bon de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBM(String referenceBM) throws TopazeException;

	/**
	 * marquer les biens a migre comme prepare.
	 * 
	 * @param bonMigration
	 *            le {@link BonPreparation} global dont les biens a migre seront marquer comme prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBienMigrationPreparer(final BonPreparation bonMigration) throws TopazeException;

	/**
	 * initier un bon de cession.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBC(BonPreparation bonPreparation) throws TopazeException;

	/**
	 * marquer un {@link ElementLivraison} cede.
	 * 
	 * @param referenceElementCession
	 *            reference element cession.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerECCede(String referenceElementCession) throws TopazeException;

	/**
	 * marquer un {@link BonPreparation} cede.
	 * 
	 * @param referenceBonCession
	 *            reference bon cession.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBCCede(String referenceBonCession) throws TopazeException;

	/**
	 * marque un bon de migration comme prepare.
	 * 
	 * @param referenceBMGlobal
	 *            reference du Bon de migration Global a prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMGlobalPrepare(String referenceBMGlobal) throws TopazeException;

	/**
	 * Chercher un bon de cession par reference.
	 * 
	 * @param referenceBC
	 *            reference du BC.
	 * @return {@link BonPreparation}
	 */
	public BonPreparation findBCByReference(String referenceBC);

	/**
	 * chercher les code produit pour les elements contractuelles.
	 * 
	 * @param elementsRenouvellemtnInfo
	 *            {@link ElementsRenouvellemtnInfo}.
	 * @return {@link ElementStateInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementStateInfo> getElementsCodeProduit(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException;

	/**
	 * chercher les code colis pour les elements contractuelles.
	 * 
	 * @param elementsRenouvellemtnInfo
	 *            {@link ElementsRenouvellemtnInfo}.
	 * @return {@link ElementStateInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementStateInfo> getElementsCodeColis(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException;

	/**
	 * Initier un bon de renouvellement.
	 * 
	 * @param bonRenouvellement
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBRE(BonPreparation bonRenouvellement) throws TopazeException;

	/**
	 * marquer un {@link ElementLivraison} renouvele.
	 * 
	 * @param referenceElementRenouvellement
	 *            reference element renouvellement.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void marquerERRenouvele(String referenceElementRenouvellement) throws TopazeException;

	/**
	 * Chercher un bon de renouvellement par reference.
	 * 
	 * @param referenceBC
	 *            reference du BC.
	 * @return {@link BonPreparation}
	 */
	public BonPreparation findBREByReference(String referenceBC);

	/**
	 * marquer un {@link BonPreparation} cede.
	 * 
	 * @param referenceBonRenouvellement
	 *            reference bon renouvellement.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalRenouvele(String referenceBonRenouvellement) throws TopazeException;

	/**
	 * marquer un element/bon non migre.
	 * 
	 * @param reference
	 *            reference du bon/element migration.
	 * @param causeNonLivraison
	 *            cause de non livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerNonMigre(String reference, String causeNonLivraison) throws TopazeException;

}
