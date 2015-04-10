package com.nordnet.topaze.contrat.test.mock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nordnet.topaze.client.rest.RestClientContratCore;
import com.nordnet.topaze.exception.TopazeException;

/**
 * @author Denden-oussama
 * @author akram-moncer
 * 
 */
public class ContratRestClientMock extends RestClientContratCore {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(ContratRestClientMock.class);

	@Override
	public void validerSerialNumber(String referenceContrat, String idClient) throws TopazeException {
		LOGGER.info(":::ws-rec:::validerSerialNumber");
	}

}
