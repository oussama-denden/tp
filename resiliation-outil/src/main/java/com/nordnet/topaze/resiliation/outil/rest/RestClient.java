package com.nordnet.topaze.resiliation.outil.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.resiliation.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.util.Constants;
import com.nordnet.topaze.resiliation.outil.util.PropertiesUtil;

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
	 * {@link RestTemplate}.
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * {@link ObjectMapper}.
	 */
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * constructeur par defaut.
	 */
	public RestClient() {

	}

	/**
	 * Chercher les informations necessaire pour simuler la resiliation global.
	 * 
	 * @param referenceContrat
	 *            reference de Contrat.
	 * @return {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ResiliationBillingInfo[] getResiliationBillingInfo(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratResiliationInfo: global");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_RESILIATION_BILLING_INFO,
						referenceContrat);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());

			}
			ResiliationBillingInfo[] resiliationBillingInfo =
					objectMapper.readValue(responseBody, ResiliationBillingInfo[].class);
			return resiliationBillingInfo;
		} catch (IOException e) {
			throw new TopazeException("erreur dans l'appel vers topaze", e);
		} catch (ResourceAccessException e) {
			throw new TopazeException("la connection vers topaze est refuse", e);
		}

	}

	/**
	 * Chercher les informations necessaire pour simuler la resiliation partiel.
	 * 
	 * @param referenceContrat
	 *            reference de Contrat.
	 * @param numEC
	 *            numEC de produit.
	 * @return {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ResiliationBillingInfo[] getResiliationBillingInfo(String referenceContrat, Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getResiliationBillingInfo: partiel");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.GET_RESILIATION_BILLING_INFO_PARTIEL, referenceContrat, numEC);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());

			}
			ResiliationBillingInfo[] resiliationBillingInfos =
					objectMapper.readValue(responseBody, ResiliationBillingInfo[].class);
			return resiliationBillingInfos;
		} catch (IOException e) {
			throw new TopazeException("erreur dans l'appel vers topaze", e);
		} catch (ResourceAccessException e) {
			throw new TopazeException("la connection vers topaze est refuse", e);
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
		// String url =
		// restPropertiesUtil.getRestURL(RestConstants.BILLING_OUTIL, RestConstants.GET_PENALITE_BILLING_INFO,
		// referenceContrat, numEC, version, engagement, periodicite, montant,
		// Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture),
		// Constants.DEFAULT_DATE_FORMAT.format(dateDebutFacturation),
		// Constants.DEFAULT_DATE_FORMAT.format(dateFinEngagement),
		// Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat));
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
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());

			}
			PenaliteBillingInfo penaliteBillingInfo = objectMapper.readValue(responseBody, PenaliteBillingInfo.class);
			return penaliteBillingInfo;
		} catch (IOException e) {
			throw new TopazeException("erreur dans l'appel vers topaze", e);
		} catch (ResourceAccessException e) {
			throw new TopazeException("la connection vers topaze est refuse", e);
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

		// String url =
		// restPropertiesUtil.getRestURL(RestConstants.BILLING_OUTIL,
		// RestConstants.GET_REMBOURSEMENT_BILLING_INFO_MONTANT_DEFINIT, periodicite, montant,
		// Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture),
		// Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat));

		URI url =
				UriComponentsBuilder.fromUriString(RestConstants.BILLING_OUTIL)
						.path(restPropertiesUtil.getPath(RestConstants.GET_REMBOURSEMENT_BILLING_INFO_MONTANT_DEFINIT))
						.queryParam("periodicite", periodicite).queryParam("montant", montant)
						.queryParam("dateDerniereFacture", Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture))
						.queryParam("dateFinContrat", Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat)).build()
						.toUri();
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());

			}
			RemboursementBillingInfo remboursementBillingInfo =
					objectMapper.readValue(responseBody, RemboursementBillingInfo.class);
			return remboursementBillingInfo;
		} catch (IOException e) {
			throw new TopazeException("erreur dans l'appel vers topaze", e);
		} catch (ResourceAccessException e) {
			throw new TopazeException("la connection vers topaze est refuse", e);
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

		// String url =
		// restPropertiesUtil.getRestURL(RestConstants.BILLING_OUTIL, RestConstants.GET_PENALITE_BILLING_INFO,
		// referenceContrat, numEC, version, periodicite, montant,
		// Constants.DEFAULT_DATE_FORMAT.format(dateDerniereFacture),
		// Constants.DEFAULT_DATE_FORMAT.format(dateDebutFacturation),
		// Constants.DEFAULT_DATE_FORMAT.format(dateFinContrat));
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
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());

			}
			RemboursementBillingInfo remboursementBillingInfo =
					objectMapper.readValue(responseBody, RemboursementBillingInfo.class);
			return remboursementBillingInfo;
		} catch (IOException e) {
			throw new TopazeException("erreur dans l'appel vers topaze", e);
		} catch (ResourceAccessException e) {
			throw new TopazeException("la connection vers topaze est refuse", e);
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