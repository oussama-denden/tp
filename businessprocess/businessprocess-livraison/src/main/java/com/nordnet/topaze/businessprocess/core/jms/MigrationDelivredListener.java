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
 * cette classe reste a l'ecoute de evennement qui indique la fin de migration a fin qu'il puisse lancer le processus de
 * facturation.
 * 
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class MigrationDelivredListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(MigrationDelivredListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {

		LOGGER.info("Reception de l'evenement: MigrationTerminated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de Bon de migration initié.
				String referenceBM = mapMessage.getString("referenceBM");
				if (referenceBM != null) {
					businessProcessService.changerDateDebutFacturation(referenceBM);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to MigrationTerminated event", exception);
			}
		}
	}
}