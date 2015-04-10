package com.nordnet.topaze.businessprocess.facture.service;

import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * The Interface FactureService.
 * 
 * @author Ahmed-Mehdi-Laabidi
 */
public interface FactureService {

	/**
	 * Premier billing.
	 * 
	 * @param contratBillingInfo
	 *            information du contrat pour le billing {@link ContratBillingInfo}.
	 * @param isMigration
	 *            true si migration.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public void calculerPremierBilling(ContratBillingInfo contratBillingInfo, Boolean isMigration)
			throws TopazeException;

	/**
	 * Billing recurrent.
	 * 
	 * @param contratBillingInfo
	 *            information du contrat pour le billing {@link ContratBillingInfo}.
	 * @return le montant du billing recurrent.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public Double calculerBillingRecurrent(ContratBillingInfo contratBillingInfo) throws TopazeException;

	/**
	 * Calcule de dernier billing.
	 * 
	 * @param contratBillingInfos
	 *            {@link ContratBillingInfo}.
	 * @param isResiliationPartiel
	 *            resiliation partiel ou global.
	 * @param calculRemboursement
	 *            calculer les rembourssement ou non.
	 * @param calculFraisResiliation
	 *            calculer les frats de resiliation ou non.
	 * @param calculFraisPenalite
	 *            calculer les frats de penalite ou non.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void calculeDernierBilling(ContratBillingInfo[] contratBillingInfos, boolean isResiliationPartiel,
			boolean calculRemboursement, boolean calculFraisResiliation, boolean calculFraisPenalite)
			throws TopazeException;

	/**
	 * Calcule de billing de migration.
	 * 
	 * @param contratBillingInfosHistorise
	 *            {@link ContratBillingInfo}.
	 * @param contratBillingInfosNouveau
	 *            {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void calculeBillingMigration(ContratBillingInfo[] contratBillingInfosHistorise,
			ContratBillingInfo[] contratBillingInfosNouveau) throws TopazeException;

	/**
	 * calcule du billing associe au penalite/remboursement/frais resilation.
	 * 
	 * @param contratBillingInfosHistorise
	 *            lis des {@link ContratBillingInfo} associe a l'ancient cntrat.
	 * @param penalite
	 *            un flag pour calcule les penalite.
	 * @param remboursement
	 *            un flag pour calcule le remboursement.
	 * @param fraisResiliation
	 *            un flag pour calcule les frais de resiliation.
	 * @param remboursementAdmnistratif
	 *            true is remboursement admnistratif.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void calculeDernierBilligMigration(ContratBillingInfo[] contratBillingInfosHistorise, boolean penalite,
			boolean remboursement, boolean fraisResiliation, boolean remboursementAdmnistratif) throws TopazeException;

	/**
	 * Calcule du billing associe au frais de cession.
	 * 
	 * @param contratBillingInfos
	 *            {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void calculeFraisCession(ContratBillingInfo[] contratBillingInfos) throws TopazeException;

	/**
	 * Calcule de billing associe au migration administrative.
	 * 
	 * @param contratBillingInfosHistorise
	 *            historise.
	 * @param contratBillingInfosMigrationAdministrative
	 *            {@link ContratBillingInfo} migre administrativement.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void calculeBillingMigrationAdministrative(ContratBillingInfo[] contratBillingInfosHistorise,
			ContratBillingInfo[] contratBillingInfosMigrationAdministrative) throws TopazeException;

}
