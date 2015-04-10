package com.nordnet.topaze.livraison.test.mock;

import org.apache.log4j.Logger;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.jms.LivraisonMessagesSender;

/**
 * Mock de la classe {@link LivraisonMessagesSender}.
 * 
 * @author akram-moncer
 * 
 */
public class LivraisonMessagesSenderMock implements LivraisonMessagesSender {

	/**
	 * Declaration log.
	 */
	private static final Logger LOGGER = Logger.getLogger(LivraisonMessagesSenderMock.class);

	@Override
	public void sendDeliveryDeliveredEvent(String referenceContrat) {
		LOGGER.info("DeliveryDelivered Event");
	}

	@Override
	public void sendDeliveryInitiatedEvent(BonPreparation bonPreparationGlobal) {
		LOGGER.info("DeliveryInitiated Event");
	}

	@Override
	public void sendSubDeliveryDeliveredEvent(BonPreparation bpGlobal) {
		LOGGER.info("SubDeliveryDelivered Event");
	}

	@Override
	public void sendSubDeliveryPreparedEvent(BonPreparation bonPreparationGlobal) {
		LOGGER.info("SubDeliveryPrepared Event");
	}

	@Override
	public void sendPrepareDeliveryEvent(BonPreparation bonPreparationGlobal) {
		LOGGER.info("PrepareDelivery Event");
	}

}
