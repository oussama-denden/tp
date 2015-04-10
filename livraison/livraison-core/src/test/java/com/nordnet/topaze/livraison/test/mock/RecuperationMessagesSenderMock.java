package com.nordnet.topaze.livraison.test.mock;

import java.util.Map;

import org.apache.log4j.Logger;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.jms.RecuperationMessagesSender;

/**
 * Mock de la class {@link RecuperationMessagesSender}.
 * 
 * @author akram-moncer
 * 
 */
public class RecuperationMessagesSenderMock implements RecuperationMessagesSender {

	/**
	 * Declaration log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RecuperationMessagesSenderMock.class);

	@Override
	public void sendReturnInitiatedEvent(BonPreparation bonRecuperation) {
		LOGGER.info("ReturnInitiated Event");
	}

	@Override
	public void sendPrepareReturnEvent(BonPreparation bonRecuperationGlobal) {
		LOGGER.info("PrepareReturn Event");
	}

	@Override
	public void sendReturnReturnedEvent(Map<String, Object> recuperationEvent) {
		LOGGER.info("ReturnReturned Event");
	}

	@Override
	public void sendSubReturnInitiatedEvent(BonPreparation bonRecuperation) {
		LOGGER.info("SubReturnInitiated Event");
	}

}
