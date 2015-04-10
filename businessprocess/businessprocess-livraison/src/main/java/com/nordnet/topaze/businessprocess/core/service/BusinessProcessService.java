package com.nordnet.topaze.businessprocess.core.service;

import org.json.JSONException;

import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Business process service.
 * 
 * @author Oussama Denden
 * 
 */
public interface BusinessProcessService {

	/**
	 * Generation d'un bon de migration a partir de {@link ContratMigrationInfo}.
	 * 
	 * @param migrationInfo
	 *            {@link ContratMigrationInfo}.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JSONException
	 *             {@link JSONException}.
	 */
	public BonPreparation genereBM(ContratMigrationInfo migrationInfo) throws TopazeException, JSONException;

	/**
	 * Initier un bon de preparation pour le nouveau contrat valider.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBP(String referenceContrat) throws TopazeException;

	/**
	 * Initier un bon de migration.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JSONException
	 *             {@link JSONException}.
	 */
	public void initierBM(String referenceContrat) throws TopazeException, JSONException;

	/**
	 * Initier un bon de renouvellement.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBRE(String referenceContrat) throws TopazeException;

	/**
	 * Initiation d'un bon de recuperation.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @param isResiliationPartiel
	 *            true si resiliation partiel.
	 * @throws JSONException
	 *             {@link JSONException}.
	 */
	public void initiationRecuperation(String referenceContrat, Integer numEC, boolean isResiliationPartiel)
			throws TopazeException;

	/**
	 * Initiation d'un bon de cession.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierCession(String referenceContrat) throws TopazeException;

	/**
	 * Changer date debut facturation.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void changerDateDebutFacturation(String referenceContrat) throws TopazeException;

	/**
	 * Preparer un bon de preparation.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBP(String referenceBP) throws TopazeException;

	/**
	 * Preparer un bon de migration.
	 * 
	 * @param referenceBM
	 *            reference de bon de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBM(String referenceBM) throws TopazeException;

	/**
	 * Marquer un bon de preparation comme livre.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalLivre(String referenceBP) throws TopazeException;

	/**
	 * Marquer un bon de migration comme livre.
	 * 
	 * @param referenceBM
	 *            reference de bon de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMLivre(String referenceBM) throws TopazeException;

	/**
	 * Marquer un bon de migration comme retourne.
	 * 
	 * @param referenceBM
	 *            reference de bon de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMRetourne(String referenceBM) throws TopazeException;

	/**
	 * Marquer un bon de migration comme prepare.
	 * 
	 * @param referenceBP
	 *            reference de bon de migration.
	 * @param typeBonPreparation
	 *            {@link TypeBonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMGlobalPreparer(String referenceBP, TypeBonPreparation typeBonPreparation)
			throws TopazeException;

	/**
	 * Marquer un bon de prepartion comme renouvle.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalRenouvele(String referenceBP) throws TopazeException;

	/**
	 * Marquer un bon de prepartion comme cede.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalCede(String referenceBP) throws TopazeException;

	/**
	 * Preparer un bon de recuperation.
	 * 
	 * @param referenceBR
	 *            reference de bon de recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBR(String referenceBR) throws TopazeException;

	/**
	 * Preparer un element de recuperation.
	 * 
	 * @param referenceBR
	 *            reference de bon de recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerER(String referenceBR) throws TopazeException;

}
