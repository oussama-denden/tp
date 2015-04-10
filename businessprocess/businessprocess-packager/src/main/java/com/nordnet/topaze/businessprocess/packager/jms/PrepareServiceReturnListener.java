package com.nordnet.topaze.businessprocess.packager.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.packager.service.PackagerService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "TraductionRecuperationService" afin de demarrer
 * le processus de traduction d'un Bon de retour.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PrepareServiceReturnListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareServiceReturnListener.class);

	/**
	 * {@link PackagerService}.
	 */
	@Autowired
	private PackagerService packagerService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareServiceReturn");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					packagerService.packagerSuspension(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("Error occurs during call of  PrepareServiceReturnListener.onMessage()", exception);
			}
		}
	}
}