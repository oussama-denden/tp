package com.nordnet.topaze.client.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.business.facturation.ReductionInfo;
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
@Component("restClientBillingOutil")
public class RestClientBillingOutil {

	/**
	 * declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RestClientBillingOutil.class);

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
	public RestClientBillingOutil() {

	}

	/**
	 * @param restPropertiesUtil
	 *            {@link RestPropertiesUtil}.
	 */
	public void setRestPropertiesUtil(RestPropertiesUtil restPropertiesUtil) {
		this.restPropertiesUtil = restPropertiesUtil;
	}

	/**
	 * Chercher les reductions valides pendanat une periode.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param dateDebut
	 *            date debut.
	 * @param dateFin
	 *            date fin.
	 * @return {@link ReductionInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ReductionInfo getReductionParPeriode(String referenceContrat, Date dateDebut, Date dateFin)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getReductionParPeriode");
		String url =
				restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_REDUCTION_PAR_PERIODE,
						referenceContrat, dateDebut, dateFin);
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
				return objectMapper.readValue(responseBody, ReductionInfo.class);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

	/**
	 * retourner les reductions associe a un element contractuel.
	 * 
	 * @param referenceContrat
	 *            reference element contractuel.
	 * @param numEC
	 *            le numero contractuel.
	 * @param version
	 *            version du contrat.
	 * @return list des reductions associe a l'element contractuel.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public List<ReductionInfo> getReductionAssocie(String referenceContrat, Integer numEC, Integer version)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getReductionAssocie");
		String url = null;
		if (version == null) {
			url =
					restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT,
							RestConstants.GET_REDUCTION_SANS_VERSION, referenceContrat, numEC, version);
		} else {
			url =
					restPropertiesUtil.getRestURL(RestConstants.BRIQUE_CONTRAT, RestConstants.GET_REDUCTION,
							referenceContrat, numEC, version);
		}
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
				ReductionInfo[] reductionInfos = objectMapper.readValue(responseBody, ReductionInfo[].class);
				return Arrays.asList(reductionInfos);
			}
		} catch (IOException exception) {
			LOGGER.error("failed to send REST request", exception);
			throw new TopazeException("Erreur dans l'envoie du demande REST: " + url, exception);
		}
	}

}
