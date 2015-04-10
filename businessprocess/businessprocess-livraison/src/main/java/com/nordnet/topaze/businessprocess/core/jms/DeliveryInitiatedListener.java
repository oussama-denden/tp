package com.nordnet.topaze.businessprocess.core.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.core.service.BusinessProcessService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "BPInitie Event" afin de demarrer le processus de
 * preparation d'un Bon de preparation.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class DeliveryInitiatedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(DeliveryInitiatedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: DeliveryInitiated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de Bon de preparation preparer.
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					businessProcessService.preparerBP(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to DeliveryInitiated event", exception);
			}
		}
	}
}