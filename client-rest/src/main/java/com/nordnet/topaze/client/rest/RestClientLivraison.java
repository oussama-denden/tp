package com.nordnet.topaze.client.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
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
import com.nordnet.topaze.client.rest.business.livraison.ChangerDateFacturationInfos;
import com.nordnet.topaze.client.rest.business.livraison.ContratBP;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.client.rest.outil.RestConstants;
import com.nordnet.topaze.client.rest.outil.RestPropertiesUtil;
import com.nordnet.topaze.client.rest.outil.RestUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;

/**
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
@Component("restClientLivraison")
public class RestClientLivraison {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(RestClientLivraison.class);

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
	public RestClientLivraison() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Initier la date debut facturation.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @return date modifier.
	 */
	public Date changerDateDebutFacturation(String referenceContrat, Date dateDebutFacturation) throws TopazeException {
		LOGGER.info(":::ws-call:::changerDateDebutFacturation");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.CHANGER_DATE_FACTURATION,
						referenceContrat);
		ChangerDateFacturationInfos request = new ChangerDateFacturationInfos();
		request.setDateDebutFacturation(dateDebutFacturation);
		try {
			HttpEntity<ChangerDateFacturationInfos> requestEntity = new HttpEntity<>(request);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				return objectMapper.readValue(responseBody, Date.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}

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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
			} else if (RestUtil.isNotFound(response.getStatusCode())) {
				InfoErreur infoErreur = new InfoErreur();
				infoErreur.setErrorCode("404");
				infoErreur.setErrorMessage("Not Found");
				infoErreur.setUrl(url);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				if (responseBody != null) {
					return objectMapper.readValue(responseBody, ContratBP.class);
				}
				return null;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Initier un Bon de Préparation a partir d'une référence du Contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBR(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::initierBR");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.INITIER_BR,
						referenceContrat);
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
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Initier un element de retour a partir d'une référence du Contrat.
	 * 
	 * @param referenceElementContractuel
	 *            reference de l'element contractuel.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierER(String referenceElementContractuel) throws TopazeException {
		LOGGER.info(":::ws-call:::initierER");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.INITIER_ER,
						referenceElementContractuel);
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
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Préparer un bon de retour.
	 * 
	 * @param bonRetour
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBR(BonPreparation bonRetour) throws TopazeException {
		LOGGER.info(":::ws-call:::preparerBR");

		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.PREPARER_BR);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonRetour);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Préparer les element de retour.
	 * 
	 * @param bonRetour
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerER(BonPreparation bonRetour) throws TopazeException {
		LOGGER.info(":::ws-call:::preparerER");

		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.PREPARER_ER);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonRetour);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Recuperer {@link BonPreparation} de type cession.
	 * 
	 * @param referenceBC
	 *            reference du BC.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonCession(String referenceBC) throws TopazeException {
		LOGGER.info(":::ws-call:::getBonPreparation");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_BONCESSION,
						referenceBC);
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
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonPreparationInfo(String referenceContrat) throws TopazeException {

		LOGGER.info(":::ws-call:::getBonPreparationInfo");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_OUTIL,
						RestConstants.GET_BONPREPARATION_INFO, referenceContrat);
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
	 * Appel vers Livraison-Core pour Initier le Bon de preparation.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBP(BonPreparation bonPreparation) throws TopazeException {

		LOGGER.info(":::ws-call:::initierBP");

		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.INITIER_BP);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonPreparation);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Appel vers Livraison-Core pour demarrer la preparation d'un Bon de preparation.
	 * 
	 * @param referenceBP
	 *            reference du Bon de Preparation
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBP(String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-call:::preparerBP");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.PREPARER_BP,
						referenceBP);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Appel vers Livraison-Core pour demarrer la preparation d'un Bon de migration.
	 * 
	 * @param referenceBM
	 *            reference du Bon de migration
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void preparerBM(String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-call:::preparerBM");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.PREPARER_BM,
						referenceBM);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Appel vers Livraison-Core pour marker un BP global preparer.
	 * 
	 * @param referenceBP
	 *            reference du BP
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalPreparer(String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBPGlobalPreparer");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.MARQUER_BPGLOBAL_PREPARE, referenceBP);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Marker un BP global comme livre.
	 * 
	 * @param referenceBP
	 *            reference du BP.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalLivre(String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBPGlobalLivre");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.MARQUER_BPGLOBAL_LIVRE, referenceBP);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * annuler un {@link BonPreparation}.
	 * 
	 * @param referenceBP
	 *            reference du BP.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void annuler(final String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-call:::annuler");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.ANNUER_BONPREPARATION,
						referenceBP);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * @return liste de BR Global non recupere.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<BonPreparation> getBRGlobalEncoursRecuperation() throws TopazeException {
		LOGGER.info(":::ws-call:::getBonPreparation");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_BRGLOBAL_ENCOURS_RECUPERATION);
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
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				BonPreparation[] bonPreparations = objectMapper.readValue(responseBody, BonPreparation[].class);
				return Arrays.asList(bonPreparations);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * marquer un {@link BonPreparation} comme recuperer.
	 * 
	 * @param referenceBR
	 *            refernce du BR.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerRecupere(String referenceBR) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerRecuperer");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_RECUPERE,
						referenceBR);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Recuperer les informations de migration d'un contrat à partir de la brique livraison outil.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratMigrationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratMigrationInfo getContratMigrationInfo(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratMigrationInfo");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_OUTIL,
						RestConstants.GET_CONTRAT_MIGRATION_INFO, referenceContrat);
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
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				if (responseBody != null) {
					return objectMapper.readValue(responseBody, ContratMigrationInfo.class);
				}
				return null;
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Initier un Bon de Migration.
	 * 
	 * @param bonMigration
	 *            {@link ContratMigrationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierBM(BonPreparation bonMigration) throws TopazeException {
		LOGGER.info(":::ws-call:::initierBM");

		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.INITIER_BM);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonMigration);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * @return liste de BM Global non recupere.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<BonPreparation> getBMGlobalEncoursMigration() throws TopazeException {
		LOGGER.info(":::ws-call:::getBonMigrationNonLivre");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_BMGLOBAL_ENCOURS_MIGRATION);
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
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			} else {
				BonPreparation[] bonPreparations = objectMapper.readValue(responseBody, BonPreparation[].class);
				return Arrays.asList(bonPreparations);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * marquer un {@link BonPreparation} comme recuperer.
	 * 
	 * @param referenceBM
	 *            refernce du BM.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMLivre(String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBMLivre");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_BM_LIVRE,
						referenceBM);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * marquer un {@link BonPreparation} comme recuperer.
	 * 
	 * @param referenceBM
	 *            refernce du BM.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMRetourne(String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBMLivre");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_BM_RETOURNE,
						referenceBM);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * initier la cession du bon de preparation à un nouveau client.
	 * 
	 * @param bonCession
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void initierCession(BonPreparation bonCession) throws TopazeException {
		LOGGER.info(":::ws-call:::initierCession");
		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.INITIER_BC);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonCession);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * marquer {@link BonPreparation} cede.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalCede(BonPreparation bonPreparation) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerELCede");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_BC_CEDE,
						bonPreparation.getReference());
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
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Appel vers Livraison-Core pour marker un BM global comme preparer.
	 * 
	 * @param referenceBM
	 *            reference du BM
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBMGlobalPreparer(String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBMGlobalPreparer");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.MARQUER_BMGLOBAL_PREPARE, referenceBM);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Appel vers Livraison-Core pour Initier le Bon de renouvellement.
	 * 
	 * @param bonRenouvellement
	 * @throws TopazeException
	 */
	public void initierBRE(BonPreparation bonRenouvellement) throws TopazeException {

		LOGGER.info(":::ws-call:::initierBRE");

		String url = restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.INITIER_BRE);
		try {
			HttpEntity<BonPreparation> requestEntity = new HttpEntity<>(bonRenouvellement);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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
	 * Recuperer {@link BonPreparation} de type cession.
	 * 
	 * @param referenceBC
	 *            reference du BC.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public BonPreparation getBonRenouvellement(String referenceBRE) throws TopazeException {
		LOGGER.info(":::ws-call:::getBonPreparation");

		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.GET_BONRENOUVELLEMENT,
						referenceBRE);
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
	 * marquer {@link BonPreparation} cede.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void marquerBPGlobalRenouvele(BonPreparation bonRenouvellement) throws TopazeException {
		LOGGER.info(":::ws-call:::marquerBPGlobalRenouvele");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE, RestConstants.MARQUER_BRE_RENOUVELE,
						bonRenouvellement.getReference());
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
				throw new TopazeException(infoErreur.getErrorMessage(), infoErreur.getErrorCode());
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