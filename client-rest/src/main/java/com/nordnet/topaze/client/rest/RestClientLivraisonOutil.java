package com.nordnet.topaze.client.rest;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.business.livraison.ContratAvenantInfo;
import com.nordnet.topaze.client.rest.business.livraison.ContratBP;
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
@Component("restClientLivraisonOutil")
public class RestClientLivraisonOutil {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientLivraisonOutil.class);

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
	public RestClientLivraisonOutil() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Recuperer les informations necessaires pour preparer un Bon de preparation.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBP getContratBP(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratBP");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_CONTRATBP,
						referenceContrat);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				return objectMapper.readValue(responseBody, ContratBP.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Recuperer l'avenant d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratAvenant}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratAvenantInfo getContratAvenant(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratAvenant");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_CONTRAT_AVENANT_INFO,
						referenceContrat);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				return objectMapper.readValue(responseBody, ContratAvenantInfo.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * get le {@link ContratBP} de l'ancient contrat apres migration.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBP getContratBPHistorique(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratBPHistorique");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_CONTRATBP_HISTORIQUE,
						referenceContrat);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				return objectMapper.readValue(responseBody, ContratBP.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}
}