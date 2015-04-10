package com.nordnet.topaze.migration.outil.rest;

import java.net.URI;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ContratMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.migration.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.migration.outil.util.Constants;
import com.nordnet.topaze.migration.outil.util.PropertiesUtil;

/**
 * Resiliation outil rest client.
 * 
 * @author Denden Oussama
 * 
 */
@Component("restClient")
public class RestClient {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(RestClient.class);

	/**
	 * {@link PropertiesUtil}.
	 */
	@Autowired
	private RestPropertiesUtil restPropertiesUtil;

	/**
	 * constructeur par defaut.
	 */
	public RestClient() {

	}

	/**
	 * Chercher les informations necessaire pour simuler la migration d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference de Contrat.
	 * @return {@link ContratMigrationSimulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratMigrationSimulationInfo getContratInfoPourSimulationMigration(String referenceContrat)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getContratInfo: partiel");
		RestTemplate rt = new RestTemplate();
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.GET_CONTRAT_INFO_POUR_SIMULATION_MIGRATION, referenceContrat);
		try {
			ResponseEntity<ContratMigrationSimulationInfo> resiliationBillingInfos =
					rt.getForEntity(url, ContratMigrationSimulationInfo.class);
			return resiliationBillingInfos.getBody();
		} catch (HttpMessageNotReadableException exception) {
			LOGGER.error("failed to send REST request", exception);
			if (exception.getCause() instanceof UnrecognizedPropertyException) {
				throw new TopazeException(exception.getMessage(), exception.getMessage());
			}
			ResponseEntity<InfoErreur> infoErreurEntity = rt.getForEntity(url, InfoErreur.class);
			InfoErreur infoErreur = infoErreurEntity.getBody();
			throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
		} catch (RestClientException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Get {@link PenaliteBillingInfo}.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero elementcontractuel.
	 * @param version
	 *            version.
	 * @param engagement
	 *            engagement.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param dateDerniereFacture
	 *            derniere date de facturation.
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @param dateFinEngagement
	 *            date fin engagement.
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @return {@link PenaliteBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public PenaliteBillingInfo getPenaliteBillingInfo(String referenceContrat, Integer numEC, Integer version,
			Integer engagement, Integer periodicite, Double montant, Date dateDerniereFacture,
			Date dateDebutFacturation, Date dateFinEngagement, Date dateFinContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getPenaliteBillingInfo");
		RestTemplate rt = new RestTemplate();
		URI url =
				UriComponentsBuilder
						.fromUriString(RestConstants.BILLING_OUTIL)
						.path(restPropertiesUtil.getPath(RestConstants.GET_PENALITE_BILLING_INFO, referenceContrat,
								numEC)).queryParam("version", version).queryParam("engagement", engagement)
						.queryParam("periodicite", periodicite).queryParam("montant", montant)
						.queryParam("dateDerniereFacture", Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture))
						.queryParam("dateDebutFacturation", Constants.DEFAULT_DATE_FORMAT.format(dateDebutFacturation))
						.queryParam("dateFinEngagement", Constants.DEFAULT_DATE_FORMAT.format(dateFinEngagement))
						.queryParam("dateFinContrat", Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat)).build()
						.toUri();
		try {
			ResponseEntity<PenaliteBillingInfo> penaliteBillingInfo = rt.getForEntity(url, PenaliteBillingInfo.class);
			return penaliteBillingInfo.getBody();
		} catch (HttpMessageNotReadableException exception) {
			LOGGER.error("failed to send REST request", exception);
			if (exception.getCause() instanceof UnrecognizedPropertyException) {
				throw new TopazeException(exception.getMessage(), exception.getMessage());
			}
			ResponseEntity<InfoErreur> infoErreurEntity = rt.getForEntity(url, InfoErreur.class);
			InfoErreur infoErreur = infoErreurEntity.getBody();
			throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
		} catch (RestClientException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Calcule de remboursement a montant definit.
	 * 
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param dateDerniereFacture
	 *            derniere date de facturation.
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @return {@link PenaliteBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public RemboursementBillingInfo getRemboursementBillingInfoMontantDefinit(Integer periodicite, double montant,
			Date dateDerniereFacture, Date dateFinContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getRemboursementBillingInfoMontantDefinit");
		RestTemplate rt = new RestTemplate();
		URI url =
				UriComponentsBuilder.fromUriString(RestConstants.BILLING_OUTIL)
						.path(restPropertiesUtil.getPath(RestConstants.GET_REMBOURSEMENT_BILLING_INFO_MONTANT_DEFINIT))
						.queryParam("periodicite", periodicite).queryParam("montant", montant)
						.queryParam("dateDerniereFacture", Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture))
						.queryParam("dateFinContrat", Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat)).build()
						.toUri();
		try {
			ResponseEntity<RemboursementBillingInfo> remboursementBillingInfo =
					rt.getForEntity(url, RemboursementBillingInfo.class);
			return remboursementBillingInfo.getBody();
		} catch (HttpMessageNotReadableException exception) {
			LOGGER.error("failed to send REST request", exception);
			if (exception.getCause() instanceof UnrecognizedPropertyException) {
				throw new TopazeException(exception.getMessage(), exception.getMessage());
			}
			ResponseEntity<InfoErreur> infoErreurEntity = rt.getForEntity(url, InfoErreur.class);
			InfoErreur infoErreur = infoErreurEntity.getBody();
			throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
		} catch (RestClientException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Get {@link RemboursementBillingInfo}.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero elementcontractuel.
	 * @param version
	 *            version.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param dateDerniereFacture
	 *            derniere date de facturation.
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @return {@link PenaliteBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public RemboursementBillingInfo getRemboursementBillingInfo(String referenceContrat, Integer numEC,
			Integer version, Integer periodicite, double montant, Date dateDerniereFacture, Date dateDebutFacturation,
			Date dateFinContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getRemboursementBillingInfo");
		RestTemplate rt = new RestTemplate();
		URI url =
				UriComponentsBuilder
						.fromUriString(RestConstants.BILLING_OUTIL)
						.path(restPropertiesUtil.getPath(RestConstants.GET_REMBOURSEMENT_BILLING_INFO,
								referenceContrat, numEC)).queryParam("version", version)
						.queryParam("periodicite", periodicite).queryParam("montant", montant)
						.queryParam("dateDerniereFacture", Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture))
						.queryParam("dateDebutFacturation", Constants.DEFAULT_DATE_FORMAT.format(dateDebutFacturation))
						.queryParam("dateFinContrat", Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat)).build()
						.toUri();
		try {
			ResponseEntity<RemboursementBillingInfo> remboursementBillingInfo =
					rt.getForEntity(url, RemboursementBillingInfo.class);
			return remboursementBillingInfo.getBody();
		} catch (HttpMessageNotReadableException exception) {
			LOGGER.error("failed to send REST request", exception);
			if (exception.getCause() instanceof UnrecognizedPropertyException) {
				throw new TopazeException(exception.getMessage(), exception.getMessage());
			}
			ResponseEntity<InfoErreur> infoErreurEntity = rt.getForEntity(url, InfoErreur.class);
			InfoErreur infoErreur = infoErreurEntity.getBody();
			throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
		} catch (RestClientException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

}