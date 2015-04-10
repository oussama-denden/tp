package com.nordnet.topaze.client.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
 * Rest Client.
 * 
 * @author Denden-oussama
 * 
 */
@Component("restClientNetDelivery")
public class RestClientNetDelivery {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientNetDelivery.class);

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
	public RestClientNetDelivery() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Appel vers Livraison Core pour marquer un bien preparer.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBienPreparer(BonPreparation bonPreparation) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBienPreparer");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_BIEN_PREPARER);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonPreparation);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
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
	 * Appel vers Livraison Core pour chercher les biens en cours de livraison.
	 * 
	 * @return lis des {@link ElementLivraison} en cours de livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getBiensEnCoursLivraison() throws TopazeException {
		LOGGER.info(":::ws-call:::getBiensEnCoursLivraison");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_BIENS_ENCOURS_LIVRAISON);

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
	 * Appel vers Livraison Core pour marker un bien comme livrer.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerSousBPLivre(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerSousBPLivrer");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_SOUSBP_LIVRE);
		try {
			HttpEntity<ElementLivraison> requestEntity = new HttpEntity<>(elementLivraison);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
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
	 * Marquer un sous BP comme non levre en indiquant le cause de non livraison.
	 * 
	 * @param referenceSousBP
	 *            reference du sous {@link BonPreparation}.
	 * @param causeNonLivraison
	 *            cause de non livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerNonLivre(String referenceSousBP, String causeNonLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerNonLivrer");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_NON_LIVRE,
						referenceSousBP, causeNonLivraison);
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
				return objectMapper.readValue(responseBody, BonPreparation.class);
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
				return objectMapper.readValue(responseBody, BonPreparation.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}
}