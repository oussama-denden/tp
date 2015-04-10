package com.nordnet.topaze.contrat.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.business.ClientInfo;
import com.nordnet.topaze.contrat.business.ContratCession;
import com.nordnet.topaze.contrat.business.ContratMigrationInfo;
import com.nordnet.topaze.contrat.business.ContratMigrationSimulationInfo;
import com.nordnet.topaze.contrat.business.ContratRenouvellementInfo;
import com.nordnet.topaze.contrat.business.ProduitRenouvellement;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.exception.TopazeException;

/**
 * La service MigrationService va contenir tous les operations en rapport avec la migration contrat.
 * 
 * @author anisselmane.
 * 
 */
public interface MigrationService {

	/**
	 * Migrer contrat.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param contratMigrationInfo
	 *            the contrat migration info
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws TopazeException
	 *             the topaze exception
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 */
	public void migrerContrat(String referenceContrat, ContratMigrationInfo contratMigrationInfo)
			throws TopazeException, JsonProcessingException, CloneNotSupportedException, JSONException;

	/**
	 * Migrer contrat future.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws JsonProcessingException
	 * @throws TopazeException
	 *             the topaze exception
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 */
	public void migrerContratFutur(final Contrat contrat)
			throws JsonProcessingException, TopazeException, IOException, CloneNotSupportedException, JSONException;

	/**
	 * Migrer contrat.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param contratMigrationInfo
	 *            the contrat migration info
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws TopazeException
	 *             the topaze exception
	 */
	public void annulerMigration(String referenceContrat, AnnulationInfo migrationAnnulationInfo)
			throws TopazeException, JsonProcessingException;

	/**
	 * ceder un contrat à un autre client.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param contratCession
	 *            {@link ContratCession}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}.
	 */
	public void cederContrat(String referenceContrat, ContratCession contratCession)
			throws TopazeException, JsonProcessingException;

	/**
	 * cession future.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws IOException
	 *             {@link IOException}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void cessionFuture(Contrat contrat) throws IOException, TopazeException;

	/**
	 * annuler une demande de cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param annulationCessionInfo
	 *            {@link AnnulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void annulerCession(String referenceContrat, AnnulationInfo annulationCessionInfo) throws TopazeException;

	/**
	 * recupere les informations de l'ancient contrat changés lors de la cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return {@link ClientInfo}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ClientInfo getInfoAvantCession(String referenceContrat) throws TopazeException;

	/**
	 * renouveler un contrat
	 * 
	 * @param refereceContrat
	 * @param contratRenouvellementInfo
	 * @throws TopazeException
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 */
	public void renouvelerContrat(String refereceContrat, ContratRenouvellementInfo contratRenouvellementInfo)
			throws TopazeException, JsonProcessingException, CloneNotSupportedException, JSONException;

	/**
	 * valider si un contrat est renouvelable ou non.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}.
	 * @param produitRenouvellements
	 *            liste des {@link ProduitRenouvellement}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void isContratRenouvelable(String referenceContrat, List<ProduitRenouvellement> produitRenouvellements)
			throws TopazeException;

	/**
	 * renouveler un contrat
	 * 
	 * @param refereceContrat
	 * @param contratRenouvellementInfo
	 * @throws TopazeException
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 */
	public void renouvelerContratFutur(final Contrat contrat)
			throws TopazeException, IOException, CloneNotSupportedException, JSONException;

	/**
	 * annuler une demande de renouvelement.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param annulationCessionInfo
	 *            {@link AnnulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void annulerRenouvellement(String referenceContrat, AnnulationInfo annulationCessionInfo)
			throws TopazeException;

	/**
	 * Information necessaire a la simulation de migration.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratMigrationSimulationInfo}.
	 * @throws TopazeException
	 *             {@link ContratMigrationSimulationInfo}.
	 */
	public ContratMigrationSimulationInfo getContratInfoPourSimulationMigration(String referenceContrat)
			throws TopazeException;

}
