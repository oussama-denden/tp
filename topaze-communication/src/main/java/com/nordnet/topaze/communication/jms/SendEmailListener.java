package com.nordnet.topaze.communication.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.communication.service.CommunicationService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable d'ecouter l'evenement "contratDelivered" pour envoyer un email contenant les information
 * d'activation.
 * 
 * @author akram-moncer
 * 
 */
public class SendEmailListener implements MessageListener {

	/**
	 * {@link CommunicationService}.
	 */
	@Autowired
	private CommunicationService communicationService;

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(SendEmailListener.class);

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContractDelivered");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de bon de preparation global.
				String referenceContrat = mapMessage.getString("referenceContrat");
				communicationService.envoyerMail(referenceContrat);

			} catch (JMSException | TopazeException e) {
				LOGGER.error("Error occurs during call of PrepareEmailListener.onMessage()", e);
			}
		}
	}
}