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
import com.nordnet.topaze.client.rest.business.contrat.ElementStateInfo;
import com.nordnet.topaze.client.rest.business.contrat.ElementsRenouvellemtnInfo;
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
@Component("restClientContratOutil")
public class RestClientContratOutil {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientContratOutil.class);

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
	public RestClientContratOutil() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Appel vers Livraison core pour recuperer les codes produit de chaque element contractuelle .
	 * 
	 * @return {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementStateInfo> getElementCodeProduits(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getElementCodeProduits");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_ELEMENTS_CODE_PRODUIT, elementsRenouvellemtnInfo);
		try {
			HttpEntity<ElementsRenouvellemtnInfo> requestEntity = new HttpEntity<>(elementsRenouvellemtnInfo);
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
			} else {
				ElementStateInfo[] elementStateInfos = objectMapper.readValue(responseBody, ElementStateInfo[].class);
				return Arrays.asList(elementStateInfos);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * Appel vers Livraison core pour recuperer les codes colis de chaque element contractuelle .
	 * 
	 * @return {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ElementStateInfo> getElementCodeColis(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getElementCodeColis");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_LIVRAISON_CORE,
						RestConstants.GET_ELEMENTS_CODE_COLIS, elementsRenouvellemtnInfo);

		try {
			HttpEntity<ElementsRenouvellemtnInfo> requestEntity = new HttpEntity<>(elementsRenouvellemtnInfo);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			if (RestUtil.isError(response.getStatusCode())) {
				InfoErreur infoErreur = objectMapper.readValue(responseBody, InfoErreur.class);
				throw new TopazeException(infoErreur.getErrorCode(), infoErreur.getErrorMessage());
			}
			ElementStateInfo[] elementStateInfos = objectMapper.readValue(responseBody, ElementStateInfo[].class);
			return Arrays.asList(elementStateInfos);
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

}