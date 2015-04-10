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
import com.nordnet.topaze.client.rest.business.livraison.ElementContractuelInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.business.livraison.PackagerInfo;
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
@Component("restClientPackager")
public class RestClientPackager {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(RestClientPackager.class);

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
	public RestClientPackager() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Appel vers Livraison Core pour marquer une service preparer.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerServicePrepare(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerServicePrepare");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.MARQUER_SERVICE_PREPARER);
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
	 * Appel vers Livraison Core pour chercher les services en cours d'activation.
	 * 
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getServicesEnCoursActivation() throws TopazeException {
		LOGGER.info(":::ws-call:::getServicesEnCoursActivation");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_SERVICES_ENCOURS_ACTIVATION);

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
	 * Appel vers Livraison Core pour marker un service comme active.
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
	 * Chercher la liste des services en cours du suspension.
	 * 
	 * @return liste des services en cours du suspension.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getServicesEnCoursSuspension() throws TopazeException {
		LOGGER.info(":::ws-call:::getServicesEnCoursSuspension");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_SERVICES_ENCOURS_SUSPENSION);
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
	 * Recuperer la liste des services suspendu.
	 * 
	 * @return liste des services suspendu.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementLivraison> getServicesSuspendu() throws TopazeException {
		LOGGER.info(":::ws-call:::getServicesSuspendu");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_SERVICES_SUSPENDU);
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
	 * marquer un sous {@link BonPreparation} comme recuperer.
	 * 
	 * @param referenceSousBR
	 *            reference du sous {@link BonPreparation} a marquer comme recuperer.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerSuspendu(String referenceSousBR) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerSuspendu");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_SUSPENDU,
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
				if (!infoErreur.getErrorCode().equals("3.1.2")) {
					throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
				}
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
	 * Marquer un bon/element non migre.
	 * 
	 * @param reference
	 *            reference du bon/element de migration
	 * @param causeNonLivraison
	 *            cause de non livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerNonMigre(String reference, String causeNonLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerNonMigre");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_NON_MIGRE,
						reference, causeNonLivraison);
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
	 * 
	 * @param referenceElementContractuel
	 *            la reference de l'element contractuel.
	 * @return {@link PackagerInfo}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public PackagerInfo getPackagerInfo(String referenceElementContractuel) throws TopazeException {
		LOGGER.info(":::ws-call:::getPackagerInfo");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_PACKAGER_INFO,
						referenceElementContractuel);
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
				return objectMapper.readValue(responseBody, PackagerInfo.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Retourner les fils de type 'SERVICE' d'un element Contractuel.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param numEC
	 *            numero de l'element contractuel.
	 * @return les fils de type 'SERVICE'
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public ElementContractuelInfo[] getFilsService(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::getFilsService");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_FILS_SERVICE,
						referenceContrat, numEC);
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
				ElementContractuelInfo[] elementContractuelInfos =
						objectMapper.readValue(responseBody, ElementContractuelInfo[].class);
				return elementContractuelInfos;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * 
	 * @param referenceElementLivraison
	 *            reference de l'element a retourner
	 * @return {@link ElementLivraison}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public ElementLivraison getElementLivraison(String referenceElementLivraison, String referenceProduit)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getElementLivraison");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_ELEMENT_LIVRAISON,
						referenceElementLivraison, referenceProduit);
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
					return objectMapper.readValue(responseBody, ElementLivraison.class);
				}
				return null;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Chercher un element de recuperation par reference.
	 * 
	 * @param referenceER
	 *            reference du ER.
	 * @return {@link ElementLivraison} de recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public ElementLivraison getElementRecuperation(String referenceER) throws TopazeException {
		LOGGER.info(":::ws-call:::getElementLivraison");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_ER, referenceER);
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
					return objectMapper.readValue(responseBody, ElementLivraison.class);
				}
				return null;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Retourner la reference de l'element contractuel parent.
	 * 
	 * @param refContrat
	 *            reference contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @return {@link ElementContractuelInfo} associe au parent.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public ElementContractuelInfo getParentInfo(String refContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::getReferenceParent");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_PARENT_INFO, refContrat,
						numEC);
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
				return objectMapper.readValue(responseBody, ElementContractuelInfo.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Retourner la reference du bon de preparation parent.
	 * 
	 * @param referenceEL
	 * @return
	 * @throws TopazeException
	 */
	public String getReferenceBonPreparationParent(String referenceEL, String referenceProduit, Boolean isRetour)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getBonPreparationParent");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_REF_PARENT,
						referenceEL, referenceProduit, isRetour);
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
				return responseBody;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Verifie si un element contractuel est resilier ou pas.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @return true si l'element contractuel est resilier.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public Boolean isElementContractuelResilier(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-call:::isResilier");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.IS_ELEMENT_CONTRACTUEL_RESILIER, referenceContrat, numEC);
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
				return objectMapper.readValue(responseBody, Boolean.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Appel du service 'checkIsPackagerCreationPossible' de la briqe contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return true si le test 'isPackagerCreationPossible' doit etre fait.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Boolean checkIsPackagerCreationPossible(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::checkIsPackagerCreationPossible");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
						RestConstants.CHECK_IS_PACKAGER_CREATION_POSSIBLE, referenceContrat);
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
				return objectMapper.readValue(responseBody, Boolean.class);
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
	 * Appel vers Livraison Core pour marker un element de migration comme livre.
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
	 * Appel vers Livraison Core pour marker un element de migration comme retourne.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerEMRetourne(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerEMRetourne");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_EM_RETOURNE);
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
	 * appel vers la brique contrat-core pour recuperer la reference commande associe.
	 * 
	 * @param referenceContrat
	 *            reference contrat global.
	 * @return reference commande.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String getReferenceCommande(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getReferenceCommande");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_REFERENCE_COMMANDE,
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
				return responseBody;
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
				return objectMapper.readValue(responseBody, BonPreparation.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}
}