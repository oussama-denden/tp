package com.nordnet.topaze.logger.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.business.TracageInfo;
import com.nordnet.topaze.logger.util.Constants;
import com.nordnet.topaze.logger.util.spring.ApplicationContextHolder;

/**
 * Log Rest client.
 * 
 * @author anisselmane.
 * 
 */
@Component("restClient")
public class RestClient {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(RestClient.class);

	/**
	 * {@link ObjectMapper}.
	 */
	@Autowired
	private ObjectMapper objectMapperLogger;

	/**
	 * Alert service.
	 */
	private SendAlert sendAlert;

	/**
	 * constructeur par defaut.
	 */
	public RestClient() {

	}

	/**
	 * Ajouter le log.
	 * 
	 * @param target
	 *            produit
	 * @param key
	 *            reference
	 * @param descr
	 *            description
	 * @param ip
	 *            ip
	 * @param user
	 *            user
	 * @param type
	 *            type log
	 * @throws OpaleException
	 *             {@link OpaleException}
	 */
	public void addLog(String target, String key, String descr, String ip, String user, String type)
			throws TopazeException {
		try {
			LOGGER.info(":::ws-call:::addLog");

			TracageInfo tracageInfo = new TracageInfo();
			tracageInfo.setDescription(descr);
			tracageInfo.setIp(ip);
			tracageInfo.setKey(key);
			tracageInfo.setTarget(target);
			tracageInfo.setType(type);
			tracageInfo.setUser(user);

			ResponseEntity<String> response = null;
			String url = (System.getProperty("log.url").toString());
			RestTemplate restTemplate = new RestTemplate();

			try {

				HttpEntity<TracageInfo> requestEntity = new HttpEntity<TracageInfo>(tracageInfo);
				response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
				String responseBody = response.getBody();
				if (RestUtil.isError(response.getStatusCode())) {
					LOGGER.error("failed to send request log" + response.getStatusCode() + " " + responseBody);
					getSendAlert().send(System.getProperty(Constants.PRODUCT_ID),
							"error occurs during call of log web service", "caused by " + response.getStatusCode(),
							responseBody);
				}
			} catch (RestClientException e) {
				LOGGER.error("failed to send REST request log", e);
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID),
						"error occurs during call of log web service",
						"caused by " + e.getCause().getLocalizedMessage(), e.getMessage());
			}

		} catch (Exception e) {
			LOGGER.error("failed to send REST request log", e);
		}
	}

	/**
	 * Get send alert.
	 * 
	 * @return {@link #sendAlert}
	 */
	private SendAlert getSendAlert() {
		if (this.sendAlert == null) {
			if (System.getProperty("alert.useMock").equals("true")) {
				sendAlert = (SendAlert) ApplicationContextHolder.getBean("sendAlertMock");
			} else {
				sendAlert = (SendAlert) ApplicationContextHolder.getBean("sendAlert");
			}
		}
		return sendAlert;
	}

}
