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
 * 
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "BRInitie Event" afin de demarrer le processus de
 * preparation d'un bon de retour.
 * 
 * @author anisselmane.
 * 
 */
public class ReturnInitiatedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ReturnInitiatedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ReturnInitiated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de Bon de migration initié.
				String referenceBP = mapMessage.getString("referenceBP");

				if (referenceBP != null) {
					businessProcessService.preparerBR(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to ReturnInitiated event", exception);
			}
		}
	}
}