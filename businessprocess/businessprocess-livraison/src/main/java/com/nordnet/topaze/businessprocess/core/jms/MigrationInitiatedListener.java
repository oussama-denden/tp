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
public class MigrationInitiatedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(MigrationInitiatedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: MigrationInitiated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de Bon de migration initié.
				String referenceBM = mapMessage.getString("referenceBM");

				if (referenceBM != null) {
					// Appel vers la brique Livraison Core pour preparer le bon de Preparation de reference indique
					businessProcessService.preparerBM(referenceBM);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to MigrationInitiated event", exception);
			}
		}
	}
}