package com.nordnet.topaze.client.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
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
@Component("restClientNetRetour")
public class RestClientNetRetour {

	/**
	 * declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientNetRetour.class);

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
	public RestClientNetRetour() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * marquer un sous {@link BonPreparation} comme retourner.
	 * 
	 * @param referenceSousBR
	 *            reference du sous {@link BonPreparation} a marquer comme retourne.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void markerRetourner(String referenceSousBR) throws TopazeException {
		LOGGER.info(":::ws-call:::markerRetourner");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_RETOURNER,
						referenceSousBR);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
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
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Appel vers Livraison Core pour chercher les biens en cours de recuperation.
	 * 
	 * @return {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getBiensEnCoursRecuperation() throws TopazeException {
		LOGGER.info(":::ws-call:::getBiensEnCoursRecuperation");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_BIENS_ENCOURS_RECUPERATION);

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
				ElementLivraison[] elementLivraisons = objectMapper.readValue(responseBody, ElementLivraison[].class);
				return Arrays.asList(elementLivraisons);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * marquer {@link ElementLivraison} renouvele.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerELRenouvele(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerELRenouvele");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_EL_RENOUVELE,
						elementLivraison.getReference());
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
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}

	}

	/**
	 * Recuperer {@link BonPreparation} de type recuperation de la brique Livraison-Core.
	 * 
	 * @param referenceBR
	 *            reference du BR.
	 * @return {@link BonPreparation} de recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonRecuperation(String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-call:::getBonRecuperation");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_BONRECUPERATION,
						referenceBM);
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
				if (responseBody != null) {
					return objectMapper.readValue(responseBody, BonPreparation.class);
				}
				return null;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Recuperer {@link BonPreparation} de la brique Livraison-Outil.
	 * 
	 * @param referenceBP
	 *            reference du BP.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonPreparation(String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-call:::getBonPreparation");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_BONPREPARATION,
						referenceBP);
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
				if (responseBody != null) {
					return objectMapper.readValue(responseBody, BonPreparation.class);
				}
				return null;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Recuperer {@link BonPreparation} de type migration de la brique Livraison-Core.
	 * 
	 * @param referenceBM
	 *            reference du BM.
	 * @return {@link BonPreparation} de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonMigration(String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-call:::getBonMigration");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_BONMIGRATION,
						referenceBM);
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
				if (responseBody != null) {
					return objectMapper.readValue(responseBody, BonPreparation.class);
				}
				return null;

			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

}