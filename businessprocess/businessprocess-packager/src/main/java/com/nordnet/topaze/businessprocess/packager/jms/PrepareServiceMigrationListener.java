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
 * Cette classe reste a l'ecoute de l'evenement "PrepareServiceMigration" afin de traduire au packager l'ordre de
 * migration.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PrepareServiceMigrationListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareServiceMigrationListener.class);

	/**
	 * {@link PackagerService}.
	 */
	@Autowired
	private PackagerService packagerService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareServiceMigration");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					// traduction d'un BP global vers packager.
					packagerService.traductionPackagerPourMigration(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to PrepareServiceDelivery event", exception);
			}
		}
	}
}