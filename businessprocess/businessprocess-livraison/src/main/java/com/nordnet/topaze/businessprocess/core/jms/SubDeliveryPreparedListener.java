package com.nordnet.topaze.businessprocess.core.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.core.service.BusinessProcessService;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement SousBPPreparer.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class SubDeliveryPreparedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(SubDeliveryPreparedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: SubDeliveryPrepared");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				TypeBonPreparation typeBonPreparation =
						TypeBonPreparation.fromString(mapMessage.getString("typeBonPreparation"));
				if (referenceBP != null) {
					businessProcessService.marquerBMGlobalPreparer(referenceBP, typeBonPreparation);
				}
			} catch (JMSException | TopazeException e) {
				LOGGER.error("JMS error while listening to SubDeliveryPrepared event", e);
			}
		}
	}
}