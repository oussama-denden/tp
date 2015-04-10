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
 * Cette classe reste a l'ecoute de l'evenement "TraductionLivraisonService" pour commencer la traduction vers packager.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PrepareServiceDeliveryListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareServiceDeliveryListener.class);

	/**
	 * {@link PackagerService}.
	 */
	@Autowired
	private PackagerService packagerService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareServiceDelivery");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de bon de preparation global.
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					// traduction d'un BP global vers packager.
					packagerService.traductionPackagerPourLivraison(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to PrepareServiceDelivery event", exception);
			}
		}
	}
}