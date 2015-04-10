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
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "Contrat Valide Event" afin de demarrer le
 * processus d'initiation d'un Bon de preparation.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ContratValidatedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ContratValidatedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContratValidated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			String referenceContrat;
			try {
				// recuperation reference contrat validee.
				referenceContrat = mapMessage.getString("referenceContrat");
				if (referenceContrat != null) {
					businessProcessService.initierBP(referenceContrat);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to ContratValidated event", exception);
			}
		}
	}
}