package com.nordnet.topaze.client.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.PenaliteBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.RemboursementBillingInfo;
import com.nordnet.topaze.client.rest.business.util.Constants;
import com.nordnet.topaze.client.rest.outil.RestConstants;
import com.nordnet.topaze.client.rest.outil.RestPropertiesUtil;
import com.nordnet.topaze.client.rest.outil.RestUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;

/**
 * @author Denden-oussama
 * @author akram-moncer
 * 
 */
@Component("restClientFacture")
public class RestClientFacture {

	/**
	 * declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientFacture.class);

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
	public RestClientFacture() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * retourne les references des contrat valides.
	 * 
	 * @return {@link List<String>}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getReferencesContratsGlobalValider() throws TopazeException {
		LOGGER.info(":::ws:::getReferencesContratsGlobalValider");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.GET_REFERENCES_CONTRATS_GLOBAL_VALIDER);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, List.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat ",
					exception);
		}
	}

	/**
	 * Get contrat billing information by reference.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @return {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBillingInfo[] getContratBillingInformation(String referenceContrat, Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getContratBillingInformation");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.GET_CONTRAT_BILLING_INFORMATION, referenceContrat, numEC);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, ContratBillingInfo[].class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * 
	 * @return listes des references des contrats livrer non resilier.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@SuppressWarnings({ "unchecked" })
	public List<String> getReferencesContratLivrer() throws TopazeException {
		LOGGER.info(":::ws-call:::getReferencesContratLivrer");
		String url =
				restPropertiesUtil
						.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_REFERENCES_CONTRAT_LIVRER);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, List.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * 
	 * @param referenceContrat
	 *            reference de contrat global ou de sous contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void changerDateDerniereFacture(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::changerDateDerniereFacture");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.CHANGER_DATE_DERNIERE_FACTURE, referenceContrat, numEC);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			if (RestUtil.isError(response.getStatusCode())) {
				String responseBody = response.getBody();
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * 
	 * @param referenceContrat
	 *            reference de contrat global ou de sous contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void changerDateFactureResiliation(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::changerDateDerniereFacture");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.CHANGER_DATE_FACTURE_RESILIATION, referenceContrat, numEC);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			if (RestUtil.isError(response.getStatusCode())) {
				String responseBody = response.getBody();
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * 
	 * @param referenceContrat
	 *            reference de contrat global ou de sous contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void changerDateFinContrat(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::changerDateFinContrat");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.CHANGER_DATE_FIN_CONTRAT,
						referenceContrat, numEC);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			if (RestUtil.isError(response.getStatusCode())) {
				String responseBody = response.getBody();
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * Utiliser une reduction: augmenter son nombre d'utilisation de 1.
	 * 
	 * @param referenceContrat
	 *            reference contrat ou element contractuel.
	 * @param numEC
	 *            numero element contractuel.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void utiliserReduction(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::utiliserReduction");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.UTILISER_REDUCTION,
						referenceContrat, numEC);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			if (RestUtil.isError(response.getStatusCode())) {
				String responseBody = response.getBody();
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * retourne les {@link ContratBillingInfo} de la derniere historisation du contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBillingInfo[] getContratBillingInformationHistorise(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratBillingInformationHistorise");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.GET_CONTRAT_BILLING_INFORMATION_HISTORISE, referenceContrat);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, ContratBillingInfo[].class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("BusinessProcess-facture :Erreur dans l'envoie du demande rest vers Contrat",
					exception);
		}
	}

	/**
	 * {@link ContratBillingInfo} pour la migration administrative.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return liste des {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBillingInfo[] getContratMigrationAdministrativeBillingInformation(String referenceContrat)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getContratBillingInformation");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.GET_CONTRAT_MIGRATION_ADMINISTRATIVE_BILLING_INFORMATION, referenceContrat);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, ContratBillingInfo[].class);
			}
		} catch (IOException exception) {
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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(RestConstants.BILLING_OUTIL
						+ restPropertiesUtil.getPath(RestConstants.GET_PENALITE_BILLING_INFO, referenceContrat, numEC));
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, PenaliteBillingInfo.class);
			}
		} catch (IOException exception) {
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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(RestConstants.BILLING_OUTIL
						+ restPropertiesUtil.getPath(RestConstants.GET_REMBOURSEMENT_BILLING_INFO, restPropertiesUtil
								.getPath(RestConstants.GET_REMBOURSEMENT_BILLING_INFO_MONTANT_DEFINIT)));
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, RemboursementBillingInfo.class);
			}
		} catch (IOException exception) {
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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(RestConstants.BILLING_OUTIL
						+ restPropertiesUtil.getPath(RestConstants.GET_REMBOURSEMENT_BILLING_INFO, referenceContrat,
								numEC));
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else {
				return objectMapper.readValue(responseBody, RemboursementBillingInfo.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

}