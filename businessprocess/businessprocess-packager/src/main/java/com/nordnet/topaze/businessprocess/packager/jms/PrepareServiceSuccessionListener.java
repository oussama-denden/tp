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
 * listener de l'evenement 'PrepareServiceSuccession' pour effectuer la cession d'un service.
 * 
 * @author akram-moncer
 * 
 */
public class PrepareServiceSuccessionListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareServiceSuccessionListener.class);

	/**
	 * {@link PackagerService}.
	 */
	@Autowired
	private PackagerService packagerService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareServiceSuccession");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					packagerService.traductionPourCession(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("error occurs during call of prepare service succession Listener", exception);
			}
		}
	}
}