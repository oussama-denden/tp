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
import com.nordnet.topaze.client.rest.business.livraison.ClientInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.outil.RestConstants;
import com.nordnet.topaze.client.rest.outil.RestPropertiesUtil;
import com.nordnet.topaze.client.rest.outil.RestUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Rest Client.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Component("restClientSwap")
public class RestClientSwap {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientSwap.class);

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
	public RestClientSwap() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Appel vers Livraison Core pour chercher les biens en cours de migration.
	 * 
	 * @return liste des {@link ElementLivraison} en cours de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getElementMigrationEnCoursLivraison() throws TopazeException {
		LOGGER.info(":::ws-call:::getElementMigrationEnCoursLivraison");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_BIEN_ENCOURS_MIGRATION);

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
	 * Appel vers Livraison Core pour marker un bien de migration comme livre.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerEMLivre(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerEMLivre");
		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_EM_LIVRE);
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
	 * Appel vers Livraison Core pour chercher les biens de migration en cours de retourne.
	 * 
	 * @return liste des {@link ElementLivraison} en cours de retour.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getElementEnCoursRetour() throws TopazeException {
		LOGGER.info(":::ws-call:::getBiensEnCoursRetour");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_BIEN_ENCOURS_RETOUR);

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
	 * Appel vers Livraison Core pour marker un bien de migration comme retourné.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerEMRetourne(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerEMRetourne");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_EM_RETOUR);
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
	 * Appel vers Livraison Core pour marquer les elements de migration comme prepare.
	 * 
	 * @param bonMigration
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBienMigrationPrepare(BonPreparation bonMigration) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBienMigrationPrepare");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.MARQUER_BIEN_MIGRATION_PREPARE);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonMigration);
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
	 * marquer {@link ElementLivraison} cede.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerELCede(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerELCede");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_EC_CEDE,
						elementLivraison.getReference());
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
	 * retouner les info du contrat avant la cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return {@link ClientInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ClientInfo getInfoAvantCession(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getInfoAvantCession");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_INFO_AVANT_CESSION,
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
				return objectMapper.readValue(responseBody, ClientInfo.class);
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