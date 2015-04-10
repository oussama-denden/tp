package com.nordnet.topaze.client.rest;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.nordnet.topaze.client.rest.outil.RestUtil;

/**
 * un response handler customise.
 * 
 * @author akram-moncer.
 * 
 */
public class RestClientResponseErrorHandler implements ResponseErrorHandler {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(RestClientResponseErrorHandler.class);

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		LOGGER.error("Response error: {" + response.getStatusCode() + "} {" + response.getStatusText() + "}");
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return RestUtil.isError(response.getStatusCode());
	}

}
