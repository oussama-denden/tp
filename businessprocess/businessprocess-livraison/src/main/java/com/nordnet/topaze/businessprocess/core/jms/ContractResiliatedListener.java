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
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "RIC Event" afin de demarrer le processus de
 * resiliation à l'Initiative de client.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ContractResiliatedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ContractResiliatedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContractResiliated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceContrat = mapMessage.getString("referenceContrat");
				Integer numEC = mapMessage.getInt("numEC");
				boolean isResiliationPartiel = mapMessage.getBoolean("isResiliationPartiel");
				if (referenceContrat != null) {
					businessProcessService.initiationRecuperation(referenceContrat, numEC, isResiliationPartiel);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to ContractResiliated event", exception);
			}
		}
	}
}